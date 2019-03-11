import sys
import os
import time
import urlparse
from PySide.QtCore import *
from PySide.QtGui import *
from PySide.QtWebKit import *
from PySide.QtNetwork import QNetworkAccessManager

class mybrowser():
    def __init__(self):
        self.user_agent = 'Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2) Gecko/20100124 Firefox/3.6 (Swiftfox)'

        self.application = QApplication([])
        self.webpage = QWebPage()
        self.network_manager = QNetworkAccessManager()
        self.network_manager.createRequest = self._create_request
        self.webpage.setNetworkAccessManager(self.network_manager)
        self.webpage.userAgentForUrl = lambda url: self.user_agent
        self.webframe = self.webpage.currentFrame()

        self.webpage.javaScriptAlert = self._alert
        self.webpage.javaScriptConfirm = self._alert
        self.url = ''
        self._load_status = 'init'
        self.web_view = QWebView()
        self.web_view.setPage(self.webpage)
        self.web_view.loadFinished.connect(self.onLoadFinished)
        # self.webpage.connect(self.webpage, SIGNAL('loadFinished()'), self.onLoadFinished)
    def _create_request(self, operation, request, data):
        reply = QNetworkAccessManager.createRequest(self.network_manager,
                                                    operation,
                                                    request,
                                                    data)
        url=request.url().toString()
        print(url)
        return reply

    def load(self, url):
        self.url = url
        self.webframe.load(QUrl(url))
        self.wait_load()

    def html(self):
        return unicode(self.webframe.toHtml())

    def wait_load(self, least=0, most=30):
        start = time.time()
        while ( time.time() - start < most) or time.time() - start < least:
            self.application.processEvents()
            time.sleep(0.1)

    def onLoadFinished(self):
        #print("finished")
        sys.exit(0)

    def _alert(self, frame, message):
        pass

    def close(self):
        """Close Browser instance and release resources."""
        sys.exit(0)


if __name__ == '__main__':
    try:
        browser = mybrowser()
        url = sys.argv[1]
        browser.load(url)
        browser.close()
    except Exception,e:
        print e


