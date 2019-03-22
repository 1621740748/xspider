import os
def getAutoLoadUrl(url):
    resultList=[]
    result = os.popen('python br.py '+url)
    res = result.read()
    for line in res.splitlines():
        if line !=None and line.startswith("http"):
            resultList.append(line)
    return resultList


l=getAutoLoadUrl("http://fund.jrj.com.cn");
print(20*'--')
print('\n'.join(l))
print len(l)

l=getAutoLoadUrl("https://fund.jrj.com.cn");
print(20*'--')
print('\n'.join(l))
print len(l)