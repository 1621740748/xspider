import sys
from PySide.QtCore import *
from PySide.QtGui import QApplication
from PySide.QtGui import QMainWindow
from PySide.QtWebKit import QWebView, QWebPage
from PySide.QtNetwork import QNetworkAccessManager
import threading
import thread
class Browser:
    def __init__(self):
        self.network_manager = QNetworkAccessManager()
        self.network_manager.createRequest = self._create_request

        self.web_page = QWebPage()
        self.web_page.setNetworkAccessManager(self.network_manager)

        self.web_view = QWebView()
        self.web_view.setPage(self.web_page)

        self.html_data = None
        self.resultList=[]
        self.finished=threading.Event()

    def _create_request(self, operation, request, data):
        reply = QNetworkAccessManager.createRequest(self.network_manager,
                                                    operation,
                                                    request,
                                                    data)
        self.resultList.append(request.url().toString())
        return reply

    def myLoadFinish(self):
         #print "finished"
        self.finished.set()

    def getResourceUrlList(self,url):
        self.web_view.load(url);
        self.web_view.loadFinished.connect(self.myLoadFinish);
        self.finished.wait()
        return self.resultList;

def execBrowser():
    browser = Browser()
    l=browser.getResourceUrlList("http://fund.jrj.com.cn");
    print('\n'.join(l))
if __name__ == '__main__':
    app = QApplication(sys.argv)
    thread.start_new_thread(execBrowser,())
    app.exec_()
    # print 30*'*'
    # c=browser.getResourceUrlList("http://stock.jrj.com.cn");
    # print('\n'.join(c))

    # while True:
    #     print "aaaa"
    #     sleep(1)
