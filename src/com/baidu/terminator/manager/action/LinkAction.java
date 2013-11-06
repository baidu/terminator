/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.form.LinkForm;
import com.baidu.terminator.manager.service.LinkService;

@Controller("linkAction")
@RequestMapping(value = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
public class LinkAction extends BaseAction {

	@Resource(name = "linkService")
	private LinkService linkService;

	/**
	 * List all links.
	 * 
	 * @return all links
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Link>> getAllLink() {
		List<Link> allLink = linkService.getAllLink();
		return new ResponseEntity<List<Link>>(allLink, HttpStatus.OK);
	}

	/**
	 * Add a link.
	 * 
	 * @param linkForm
	 *            the link form , for example:
	 *            {"name":"http_proxy","localPort":8080
	 *            ,"remoteAddress":"61.135.169.125"
	 *            ,"remotePort":80,"workMode":"RECORD"
	 *            ,"protocolType":"HTTP","storageType":"CACHE","signClass":
	 *            "com.baidu.terminator.plugin.signer.DefaultHttpSigner"
	 *            ,"extractClass"
	 *            :"com.baidu.terminator.plugin.extractor.DefaultHttpExtractor"}
	 * @return the new created resource location in HTTP header
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> addLink(@Valid @RequestBody LinkForm linkForm) {
		Link link = new Link();
		link.setName(linkForm.getName());
		link.setLocalPort(linkForm.getLocalPort());
		link.setRemoteAddress(linkForm.getRemoteAddress());
		link.setRemotePort(linkForm.getRemotePort());
		link.setWorkMode(linkForm.getWorkMode());
		link.setProtocolType(linkForm.getProtocolType());
		link.setStorageType(linkForm.getStorageType());
		link.setSignClass(linkForm.getSignClass());
		link.setExtractClass(linkForm.getExtractClass());
		int id = linkService.addLink(link);

		return new ResponseEntity(setLocationHeaders("/link/" + id),
				HttpStatus.CREATED);
	}

	/**
	 * Update a link.
	 * 
	 * @param linkId
	 * @param linkForm
	 *            the link form , for example:
	 *            {"name":"http_proxy","localPort":8080
	 *            ,"remoteAddress":"61.135.169.125"
	 *            ,"remotePort":80,"workMode":"RECORD"
	 *            ,"protocolType":"HTTP","storageType":"CACHE","signClass":
	 *            "com.baidu.terminator.plugin.signer.DefaultHttpSigner"
	 *            ,"extractClass"
	 *            :"com.baidu.terminator.plugin.extractor.DefaultHttpExtractor"}
	 */
	@RequestMapping(value = "/{linkId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateLink(@PathVariable int linkId,
			@Valid @RequestBody LinkForm linkForm) {
		Link link = new Link();
		link.setId(linkId);
		link.setName(linkForm.getName());
		link.setLocalPort(linkForm.getLocalPort());
		link.setRemoteAddress(linkForm.getRemoteAddress());
		link.setRemotePort(linkForm.getRemotePort());
		link.setWorkMode(linkForm.getWorkMode());
		link.setProtocolType(linkForm.getProtocolType());
		link.setStorageType(linkForm.getStorageType());
		link.setSignClass(linkForm.getSignClass());
		link.setExtractClass(linkForm.getExtractClass());
		linkService.modifyLink(link);
	}

	/**
	 * Query link configuration through id.
	 * 
	 * @param linkId
	 *            link id
	 * @return the configuration whose id is linkId
	 */
	@RequestMapping(value = "/{linkId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Link> getLink(@PathVariable int linkId) {
		Link link = linkService.getLinkById(linkId);
		return new ResponseEntity<Link>(link, HttpStatus.OK);
	}

	/**
	 * Query link configuration through name.
	 * 
	 * @param name
	 *            link name (fuzzy query)
	 * @return the configuration whose name like query condition
	 */
	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Link>> queryLinkByName(@RequestBody String name) {
		List<Link> links = linkService.getLinkByName(name);
		return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
	}

	/**
	 * Delete link through id.
	 * 
	 * @param linkId
	 *            link id
	 */
	@RequestMapping(value = "/{linkId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLink(@PathVariable int linkId) {
		linkService.deleteLink(linkId);
	}

	@RequestMapping(value = "/protocols", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Set<String>> getSupportedProtocols() {
		Set<String> supportedProtocols = linkService.getSupportedProtocols();
		return new ResponseEntity<Set<String>>(supportedProtocols,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/defaultSigners", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PluginInfo>> getDefaultSigners() {
		List<PluginInfo> signers = linkService.getDefaultSigners();
		return new ResponseEntity<List<PluginInfo>>(signers, HttpStatus.OK);
	}

	@RequestMapping(value = "/customizedSigners", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PluginInfo>> getCustomizedSigners() {
		List<PluginInfo> signers = linkService.getCustomizedSigners();
		return new ResponseEntity<List<PluginInfo>>(signers, HttpStatus.OK);
	}

	@RequestMapping(value = "/defaultExtractors", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PluginInfo>> getDefaultExtractors() {
		List<PluginInfo> extractors = linkService.getDefaultExtractors();
		return new ResponseEntity<List<PluginInfo>>(extractors, HttpStatus.OK);
	}

	@RequestMapping(value = "/customizedExtractors", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<PluginInfo>> getCustomizedExtractors() {
		List<PluginInfo> extractors = linkService.getCustomizedExtractors();
		return new ResponseEntity<List<PluginInfo>>(extractors, HttpStatus.OK);
	}

	@RequestMapping(value = "/portChecker/{port}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> checkPort(@PathVariable int port) {
		boolean isAvailable = linkService.isPortAvailable(port);
		if (isAvailable) {
			try {
				ServerSocket s = new ServerSocket(port);
				s.close();
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			} catch (IOException e) {
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}

}
