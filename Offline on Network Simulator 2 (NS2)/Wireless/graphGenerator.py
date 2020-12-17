import sys
readFileName = sys.argv[1]

readFile = open(readFileName)
throughputInfo = []
delayInfo = []
packetSentInfo = []
packetDropInfo = []

line = readFile.readline()
words = line.split()
xAxisLabel = words[0]
xAxisUnit = words[1]

line = readFile.readline()
xAxisValuesStr = line.split()
xAxisValues = []
for i in xAxisValuesStr:
    xAxisValues.append(int(i))

line = readFile.readline()
labels = line.split()
line = readFile.readline()
units = line.split()

for line in readFile:
    words = line.split()

    throughputInfo.append(float(words[0]))
    delayInfo.append(float(words[1]))
    packetSentInfo.append(float(words[2]))
    packetDropInfo.append(float(words[3]))

print(xAxisLabel)
print(xAxisUnit)
print(xAxisValues)
print(labels)
print(units)
print(throughputInfo)
print(delayInfo)
print(packetSentInfo)
print(packetDropInfo)

import matplotlib.pyplot as plt1

x = xAxisValues
y1 = throughputInfo
plt1.plot(x,y1)
plt1.savefig(readFileName+'Throughput.png')
plt1.clf()
plt1.plot(x,delayInfo)
plt1.savefig(readFileName+'Delay.png')
plt1.clf()
plt1.plot(x,packetSentInfo)
plt1.savefig(readFileName+'PacketSent.png')
plt1.clf()
plt1.plot(x,packetDropInfo)
plt1.savefig(readFileName+'PacketDrop.png')
plt1.clf()

