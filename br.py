import sys
from PySide.QtCore import *
from PySide.QtGui import QApplication
from PySide.QtGui import QMainWindow
from PySide.QtWebKit import QWebView, QWebPage
from PySide.QtNetwork import QNetworkAccessManager

class Browser(QMainWindow):
    def __init__(self, parent= None):
        super(Browser, self).__init__( parent)
        self.network_manager = QNetworkAccessManager()
        self.network_manager.createRequest = self._create_request

        self.web_page = QWebPage()
        self.web_page.setNetworkAccessManager(self.network_manager)

        self.web_view = QWebView()
        self.web_view.setPage(self.web_page)

        self.html_data = None

    def _create_request(self, operation, request, data):
        reply = QNetworkAccessManager.createRequest(self.network_manager,
                                                    operation,
                                                    request,
                                                    data)
        print request.url().toString()
        return reply

    def slotSourceDownloaded(self):
         reply=self.sender();
         a=QTextStream(reply).readAll()
         print a
         reply.deleteLater()
def myLoadFinish():
    print "finished"
    sys.exit(0)


if __name__ == '__main__':
    app = QApplication(sys.argv)
    browser = Browser()
    browser.web_view.load("http://fund.jrj.com.cn");
    browser.web_view.loadFinished.connect(myLoadFinish);
    app.exec_()

