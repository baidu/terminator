package com.baidu.terminator.plugin.signer;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.signer.customized.app.FromAppToDrAPIHttpRequestSigner;
import com.baidu.terminator.plugin.signer.customized.app.GZipUtil;
import com.baidu.terminator.storage.RequestResponseBundle;

public class BaseSigner implements Signer {
	private Logger logger;
	
	public BaseSigner(Link link){
		logger = LinkLogger.getLogger(FromAppToDrAPIHttpRequestSigner.class,
				link.getId());
	}
	
	@Override
	public void logResponse(RequestResponseBundle bundle) {
		HttpResponse response = (HttpResponse) bundle.getResponse();
		if (response != null) {
			ChannelBuffer content = response.getContent();
			if (content.readable()) {
				byte[] contentBytes = content.array();
				String unGzipContent = null;
				try {
					unGzipContent = GZipUtil.unGzipString(contentBytes);
				} catch (IOException e) {
					unGzipContent = new String(contentBytes);
				}
				logger.info("content of response is:" + unGzipContent);
			}
		}
	}

	@Override
	public String sign(Object request) {
		return null;
	}

	
}
