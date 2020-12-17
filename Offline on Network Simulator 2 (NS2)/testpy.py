
# sendHistory = []
# receHistory = []
# dropHistory = []
# delayDict = {}
# # print(type(delayDict))
# with open("trace1.tr") as infile:
#     i = 0
#     for line in infile:
        
#         words = line.split()
#         if(words[6] == "tcp" and words[7]=="1040" and words[3]=="RTR" and words[0] =="s"):
#             sendHistory.append(line)
#             # delayDict.add(words[5],{"startTime": words[1],"finishTime": "kill meh"})
#             delayDict[words[5]] = {"startTime": words[1],"finishTime": "kill meh"}
              
#         if(words[6] == "tcp" and words[7]=="16" and words[3]=="AGT" and words[0] =="r"):
#             receHistory.append(line)
#             delayDict[words[5]]["finishTime"] = words[1]
        
#     print(len(sendHistory))
#     print(len(receHistory))
#     dropCount = 0;
#     for line in sendHistory:
#         words = line.split()
#         found = False
#         for line2 in receHistory:
#             words2 = line2.split()
#             if(words[5]==words2[5]):
#                 found = True
#                 break
#         if(found == False):
#             dropCount = dropCount+1;

#     print(dropCount)


#     for key in delayDict:
#         # print(item)
#         print(key)
#         print(delayDict[key])

#     # print(delayDict)

#         #print(line)

#         # print(words[1])


# # with open("log.txt") as infile:
# #     for line in infile:
# #         do_something_with(line)

import sys

# print ('Number of arguments:', len(sys.argv), 'arguments.')
# print ('Argument List:', str(sys.argv))
# print(sys.argv[0])

received_packets = 0
sent_packets = 0
dropped_packets = 0
total_delay = 0
received_bytes = 0
start_time = 1000000
end_time = 0
sent_time = {}
# constants
header_bytes = 40

readFileName=sys.argv[1]
writeFileName=sys.argv[2]
graphDataFileName = sys.argv[3]

with open(readFileName) as infile:
    
    for line in infile:
        words = line.split()
        event = words[0]
        time_sec = (words[1])
        layer = words[3]
        packet_id = (words[5])
        packet_type = words[6]
        packet_bytes = (words[7])
    
        if(start_time>float(time_sec)):
            start_time = float(time_sec)

        if(layer == "AGT" and packet_type == "tcp"):
            if(event == "s"):
                sent_time[int(packet_id)] = float(time_sec)
                sent_packets += 1

            elif(event == "r"):
                delay = float(time_sec) - sent_time[int(packet_id)]
                total_delay += delay
                # print(int(packet_bytes))
                if(int(packet_bytes)==16):
                    bytes = 1040 - header_bytes
                else:
                    bytes = (int(packet_bytes) - header_bytes)
                received_bytes += bytes

                received_packets += 1

        if(packet_type == "tcp" and event == "D"):
            dropped_packets += 1  


end_time = float(time_sec)
simulation_time = end_time - start_time
# print(received_bytes)
# print( "Sent Packets: ", sent_packets)
# print("Dropped Packets: ", dropped_packets)
# print("Received Packets: ", received_packets)
# print("---------------------------")
# print("Throughtput: ", (received_bytes * 8)/ simulation_time, "bits/sec")
# print("Average Delay: ", (total_delay/received_packets), "seconds")
# print("Delivery ratio: ", (received_packets/sent_packets))
# print("Drop ratio: ", (dropped_packets/sent_packets))


writeFile = open(writeFileName,'a')

writeFile.write("\n\n")
writeFile.write("Parsed file name: "+readFileName + "\n")
writeFile.write( "Sent Packets: "+ str(sent_packets)+"\n")
writeFile.write("Dropped Packets: "+ str(dropped_packets)  +"\n")
writeFile.write("Received Packets: "+ str(received_packets) +"\n")
writeFile.write("---------------------------\n")
writeFile.write("Throughtput: "+ str((received_bytes * 8)/ simulation_time) +  " bits/sec"+"\n")
writeFile.write("Average Delay: "+ str((total_delay/received_packets)) + " seconds"+"\n")
writeFile.write("Delivery ratio: "+ str((received_packets/sent_packets)) +"\n")
writeFile.write("Drop ratio: "+ str((dropped_packets/sent_packets)) +"\n")
writeFile.write("\n\n")
writeFile.close()

writeFile = open(graphDataFileName,'a')
writeFile.write(str((received_bytes * 8)/ simulation_time) + " " + str((total_delay/received_packets)*1000)
 + " " + str((received_packets/sent_packets)*100) + " " + str((dropped_packets/sent_packets)*100) +"\n")

writeFile.close()
        
