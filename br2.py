import os
result = os.popen('python br.py')
print 30*'*'
res = result.read()
for line in res.splitlines():
    print line
    print 20*'^'
print 30*'-------'

result = os.popen('python br.py')
print 30*'*'
res = result.read()
for line in res.splitlines():
    print line
    print 20*'^'
print 30*'-------'

