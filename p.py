"""
This script reflects all content passing through the proxy.
"""
from mitmproxy import http
import re
rules=[
    ("http://([^\.]*)\.jrjimg\.cn","https://\g<1>.jrjimg.cn")
    ,("http://fund.jrj.com.cn","//fund.jrj.com.cn")
    ,("http://mapp.jrj.com.cn/pc/content/getMqNews","//mapp.jrj.com.cn/pc/content/getMqNews")
    ,("http://8.jrj.com.cn/nss/","//fund.jrj.com.cn/httpsconv/8.jrj.com.cn/nss/")
    ,("http://8.jrj.com.cn/activities","//fund.jrj.com.cn/httpsconv/8.jrj.com.cn/activities")
#    ,("http://istock.jrj.com.cn/topicJsonList.jspa","//fund.jrj.com.cn/httpsconv/istock.jrj.com.cn/topicJsonList.jspa")
    ,("http://istock.jrj.com.cn/topicJsonList.jspa","//istock.jrj.com.cn/topicJsonList.jspa")
    ,("http://sso.jrj.com.cn/","//sso.jrj.com.cn/")
    ,("http://glink.genius.com.cn","//fund.jrj.com.cn/httpsconv/glink.genius.com.cn")
    ,("http://sdc3.jrj.com.cn","//fund.jrj.com.cn/httpsconv/sdc3.jrj.com.cn")
    ,("http://news.comments.jrj.com.cn","//newscomments.jrj.com.cn")
    ,("http://js.jrj.com.cn","//js.jrj.com.cn")
    ];
def response(flow: http.HTTPFlow) -> None:
    flow.response.headers.pop('Alt-Svc', None)
    s=str(flow.response.content,"ISO-8859-1")
    for rule in rules:
        s=re.sub(rule[0],rule[1],s)
        
    flow.response.content = s.encode("ISO-8859-1")
