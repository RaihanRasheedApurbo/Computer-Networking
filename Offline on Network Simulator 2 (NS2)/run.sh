# ns wirelessCopy.tcl 40 8 5 250 20 areaSizeTrace1.tr areaSize1Animation.nam

# ns wirelessCopy.tcl 40 8 5 500 20 areaSizeTrace2.tr areaSize2Animation.nam

# ns wirelessCopy.tcl 40 8 5 750 20 areaSizeTrace3.tr areaSize3Animation.nam

# ns wirelessCopy.tcl 40 8 5 1000 20 areaSizeTrace4.tr areaSize4Animation.nam

# ns wirelessCopy.tcl 40 8 5 1250 20 areaSizeTrace5.tr areaSize5Animation.nam

# #total_node row col areaLength numberOfFlow traceFileName animationFileName
# rm areaSizeGraphData.txt
# rm areaSize.txt
# echo $'Area-Size meter-square' >> areaSizeGraphData.txt
# echo $'250 500 750 1000 1250' >> areaSizeGraphData.txt
# echo $'Network_Throughput End_To_End_Delay Packet_Delivery_Ratio Packet_Drop_Ratio' >> areaSizeGraphData.txt
# echo $'bits/sec milli-seconds % %' >> areaSizeGraphData.txt
# python3 testpy.py areaSizeTrace1.tr areaSize.txt areaSizeGraphData.txt

# python3 testpy.py areaSizeTrace2.tr areaSize.txt areaSizeGraphData.txt

# python3 testpy.py areaSizeTrace3.tr areaSize.txt areaSizeGraphData.txt

# python3 testpy.py areaSizeTrace4.tr areaSize.txt areaSizeGraphData.txt

# python3 testpy.py areaSizeTrace5.tr areaSize.txt areaSizeGraphData.txt

# # readFileName writeFileName graphDataFileName

# python3 graphGenerator.py areaSizeGraphData.txt

# #graphDataFileName




# ns wirelessCopy.tcl 20 4 5 500 20 nodeSizeTrace1.tr nodeSize1Animation.nam

# ns wirelessCopy.tcl 40 5 8 500 20 nodeSizeTrace2.tr nodeSize2Animation.nam

# ns wirelessCopy.tcl 60 6 10 500 20 nodeSizeTrace3.tr nodeSize3Animation.nam

# ns wirelessCopy.tcl 80 8 10 500 20 nodeSizeTrace4.tr nodeSize4Animation.nam

# ns wirelessCopy.tcl 100 10 10 500 20 nodeSizeTrace5.tr nodeSize5Animation.nam

# #total_node row col areaLength numberOfFlow traceFileName animationFileName
# rm nodeSizeGraphData.txt
# rm nodeSize.txt
# echo $'Node-Size unit' >> nodeSizeGraphData.txt
# echo $'20 40 60 80 100' >> nodeSizeGraphData.txt
# echo $'Network_Throughput End_To_End_Delay Packet_Delivery_Ratio Packet_Drop_Ratio' >> nodeSizeGraphData.txt
# echo $'bits/sec milli-seconds % %' >> nodeSizeGraphData.txt
# python3 testpy.py nodeSizeTrace1.tr nodeSize.txt nodeSizeGraphData.txt

# python3 testpy.py nodeSizeTrace2.tr nodeSize.txt nodeSizeGraphData.txt

# python3 testpy.py nodeSizeTrace3.tr nodeSize.txt nodeSizeGraphData.txt

# python3 testpy.py nodeSizeTrace4.tr nodeSize.txt nodeSizeGraphData.txt

# python3 testpy.py nodeSizeTrace5.tr nodeSize.txt nodeSizeGraphData.txt

# # readFileName writeFileName graphDataFileName

# python3 graphGenerator.py nodeSizeGraphData.txt

# #graphDataFileName





ns wirelessCopy.tcl 40 5 8 500 10 flowSizeTrace1.tr flowSize1Animation.nam

ns wirelessCopy.tcl 40 5 8 500 20 flowSizeTrace2.tr flowSize2Animation.nam

ns wirelessCopy.tcl 40 5 8 500 30 flowSizeTrace3.tr flowSize3Animation.nam

ns wirelessCopy.tcl 40 5 8 500 40 flowSizeTrace4.tr flowSize4Animation.nam

ns wirelessCopy.tcl 40 5 8 500 50 flowSizeTrace5.tr flowSize5Animation.nam

#total_node row col areaLength numberOfFlow traceFileName animationFileName
rm flowSizeGraphData.txt
rm flowSize.txt
echo $'Flow-Size unit' >> flowSizeGraphData.txt
echo $'10 20 30 40 50' >> flowSizeGraphData.txt
echo $'Network_Throughput End_To_End_Delay Packet_Delivery_Ratio Packet_Drop_Ratio' >> flowSizeGraphData.txt
echo $'bits/sec milli-seconds % %' >> flowSizeGraphData.txt
python3 testpy.py flowSizeTrace1.tr flowSize.txt flowSizeGraphData.txt

python3 testpy.py flowSizeTrace2.tr flowSize.txt flowSizeGraphData.txt

python3 testpy.py flowSizeTrace3.tr flowSize.txt flowSizeGraphData.txt

python3 testpy.py flowSizeTrace4.tr flowSize.txt flowSizeGraphData.txt

python3 testpy.py flowSizeTrace5.tr flowSize.txt flowSizeGraphData.txt

# readFileName writeFileName graphDataFileName

python3 graphGenerator.py flowSizeGraphData.txt

#graphDataFileName





