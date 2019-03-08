import sys
from PySide.QtCore import *
from PySide.QtGui import QApplication
from PySide.QtGui import QMainWindow
from PySide.QtWebKit import QWebView, QWebPage
from PySide.QtNetwork import QNetworkAccessManager
import threading
class Browser:
    def __init__(self, app):
        self.network_manager = QNetworkAccessManager()
        self.network_manager.createRequest = self._create_request
        self.web_page = QWebPage()
        self.web_page.setNetworkAccessManager(self.network_manager)
        self.web_view = QWebView()
        self.web_view.setPage(self.web_page)
        self.html_data = None
        self.app =app
        self.finished=threading.Event()
        self.resultList=[]
    def _create_request(self, operation, request, data):
        reply = QNetworkAccessManager.createRequest(self.network_manager,
                                                    operation,
                                                    request,
                                                    data)
        self.resultList.append(request.url().toString())
        return reply

    def myLoadFinish(self):
        app.quit()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    url=sys.argv[1]
    browser = Browser(app)
    browser.web_view.load(url);
    browser.web_view.loadFinished.connect(browser.myLoadFinish);
    app.exec_()
    print ('\n'.join(browser.resultList[1:]))

