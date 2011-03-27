package com.sharkhunter.channel;

import net.pms.dlna.virtual.VirtualFolder;
import net.pms.dlna.virtual.VirtualVideoAction;

public class ChannelPMSSaveFolder extends VirtualFolder {
	
	private Channel ch;
	private String url;
	private String name;
	private String thumb;
	private String proc;
	private int asx;
	private ChannelScraper scraper;
	private int type;
	
	public ChannelPMSSaveFolder(Channel ch,String name,String url,String thumb,
								String proc,int asx,int type,
								ChannelScraper scraper) {
		super(name,thumb);
		this.url=url;
		this.name=(ChannelUtil.empty(name)?"download":name);
		this.thumb=thumb;
		this.proc=proc;
		this.ch=ch;
		this.asx=asx;
		this.scraper=scraper;
		this.type=type;
	}
	
	public void discoverChildren() {
		addChild(new ChannelMediaStream(ch,"SAVE&PLAY",url,thumb,proc,type,asx,scraper,name,name));
		addChild(new ChannelMediaStream(ch,"PLAY",url,thumb,proc,type,asx,scraper,name,null));
		final ChannelOffHour oh=Channels.getOffHour();
		if(oh!=null) {
			final boolean add=!oh.scheduled(url);
			final String rName=name;
			addChild(new VirtualVideoAction((add?"ADD to ":"DELETE from ")+
											"offhour download", true) { //$NON-NLS-1$
				public boolean enable() {
					String rUrl=url;
					if(scraper!=null)
						rUrl=scraper.scrape(ch, url, proc, type, this);
					oh.update(rUrl, rName, add);
					return add;
				}
			});
		}
	}
}
