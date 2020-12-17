# simulator
set ns [new Simulator]


# ======================================================================
# Define options

set val(chan)         Channel/WirelessChannel  ;# channel type
set val(prop)         Propagation/TwoRayGround ;# radio-propagation model
set val(ant)          Antenna/OmniAntenna      ;# Antenna type
set val(ll)           LL                       ;# Link layer type
set val(ifq)          Queue/DropTail/PriQueue  ;# Interface queue type
set val(ifqlen)       50                       ;# max packet in ifq
set val(netif)        Phy/WirelessPhy/802_15_4          ;# network interface type
set val(mac)          Mac/802_15_4               ;# MAC type
set val(rp)           AODV                     ;# ad-hoc routing protocol 
set val(nn)           40                       ;# number of mobilenodes
# =======================================================================

# assigning value from command line
set val(r) [expr [lindex $argv 1]]       ;# number of grid size
set val(c) [expr [lindex $argv 2]]
set val(nn) [expr [lindex $argv 0]]
set areaLength [expr [lindex $argv 3]]


# trace file
set trace_file [open [lindex $argv 5] w]
$ns trace-all $trace_file

# nam file
set nam_file [open [lindex $argv 6] w]
$ns namtrace-all-wireless $nam_file $areaLength $areaLength

# topology: to keep track of node movements
set topo [new Topography]
$topo load_flatgrid $areaLength $areaLength ;# 500m x 500m area


# general operation director for mobilenodes
create-god $val(nn)


# node configs
# ======================================================================

# $ns node-config -addressingType flat or hierarchical or expanded
#                  -adhocRouting   DSDV or DSR or TORA
#                  -llType	   LL
#                  -macType	   Mac/802_11
#                  -propType	   "Propagation/TwoRayGround"
#                  -ifqType	   "Queue/DropTail/PriQueue"
#                  -ifqLen	   50
#                  -phyType	   "Phy/WirelessPhy"
#                  -antType	   "Antenna/OmniAntenna"
#                  -channelType    "Channel/WirelessChannel"
#                  -topoInstance   $topo
#                  -energyModel    "EnergyModel"
#                  -initialEnergy  (in Joules)
#                  -rxPower        (in W)
#                  -txPower        (in W)
#                  -agentTrace     ON or OFF
#                  -routerTrace    ON or OFF
#                  -macTrace       ON or OFF
#                  -movementTrace  ON or OFF

# ======================================================================

$ns node-config -adhocRouting $val(rp) \
                -llType $val(ll) \
                -macType $val(mac) \
                -ifqType $val(ifq) \
                -ifqLen $val(ifqlen) \
                -antType $val(ant) \
                -propType $val(prop) \
                -phyType $val(netif) \
                -topoInstance $topo \
                -channelType $val(chan) \
                -agentTrace ON \
                -routerTrace ON \
                -macTrace OFF \
                -movementTrace OFF

# create nodes

for {set i 0} {$i < $val(r) } {incr i} {
    for {set j 0} {$j < $val(c)} {incr j} {
        set temp [expr (($i*$val(c))+$j)]
        # puts $temp
        set node($temp) [$ns node]
        $node($temp) random-motion 1
        set destX [expr int([expr rand() * $areaLength])]
        set destY [expr int([expr rand() * $areaLength])]
        set speed [expr int([expr rand() * 5]+1)]
        # puts $destX
        # puts $destY
        # puts $speed
        
        $node($temp) set X_ [expr ($areaLength * $i) / $val(r)]
        $node($temp) set Y_ [expr ($areaLength * $j) / $val(c)]
        $node($temp) set Z_ 0
        puts 'hi'
        puts $temp
        puts $destX
        puts $destY
        puts $speed
        $ns at 1.25 "$node($temp) setdest $destX $destY $speed"
        

        $ns initial_node_pos $node($temp) 20

    }
    # set node($i) [$ns node]
    # $node($i) random-motion 0       ;# disable random motion

    # $node($i) set X_ [expr (500 * $i) / $val(nn)]
    # $node($i) set Y_ [expr (500 * $i) / $val(nn)]
    # $node($i) set Z_ 0

    # $ns initial_node_pos $node($i) 20
} 





# Traffic
set val(nf)        [expr [lindex $argv 4]]                ;# number of flows
# puts 'hi'
# puts $val(nn)
for {set i 0} {$i < $val(nf)} {incr i} {

    # set value [expr int([expr rand() * 100])]
    # set src $i
    # set dest [expr $i + 3]
    set src [expr int([expr rand() * $val(nn)])]
    set dest [expr int([expr rand() * $val(nn)])]
    while {$src == $dest} {
        # puts 'hi'
        # puts $src 
        # puts $dest
        set dest [expr int([expr rand() * $val(nn)])]
        # puts 'hi'
    }

    # puts $src 
    # puts $dest


    # Traffic config
    # create agent
    set tcp [new Agent/TCP]
    set tcp_sink [new Agent/TCPSink]
    # attach to nodes
    $ns attach-agent $node($src) $tcp
    $ns attach-agent $node($dest) $tcp_sink
    # connect agents
    $ns connect $tcp $tcp_sink
    $tcp set fid_ $i

    # Traffic generator
    set ftp [new Application/Telnet]
    # attach to agent
    $ftp attach-agent $tcp
    
    # start traffic generation
    $ns at 1.0 "$ftp start"
}



# End Simulation

# Stop nodes
for {set i 0} {$i < $val(nn)} {incr i} {
    $ns at 50.0 "$node($i) reset"
}

# call final function
proc finish {} {
    global ns trace_file nam_file
    $ns flush-trace
    close $trace_file
    close $nam_file
}

proc halt_simulation {} {
    global ns
    puts "Simulation ending"
    $ns halt
}

$ns at 50.0001 "finish"
$ns at 50.0002 "halt_simulation"




# Run simulation
puts "Simulation starting"
$ns run

