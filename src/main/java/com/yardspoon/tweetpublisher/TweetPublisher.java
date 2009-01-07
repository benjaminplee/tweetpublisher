package com.yardspoon.tweetpublisher;

import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Publisher;
import net.sourceforge.cruisecontrol.util.ValidationHelper;
import net.sourceforge.cruisecontrol.util.XMLLogHelper;

import org.jdom.Element;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetPublisher implements Publisher {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void publish(Element cruisecontrolLog) throws CruiseControlException {
		XMLLogHelper helper = new XMLLogHelper(cruisecontrolLog);
		
        String update = helper.getProjectName();
        
        if (helper.isBuildSuccessful()) {
        	update += " BUILD SUCCESSFUL"; 
        } else if (helper.isBuildFix()) {
        	update += " BUILD FIXED";
        } else {
        	update += " BUILD FAILED!";
        }
        
        try {
			new Twitter(username, password).update(update);
		} catch (TwitterException e) {
			System.err.println("UNABLE TO UPDATE STATUS VIA TWITTER: " + update);
			e.printStackTrace(System.err);
		}
	}

	public void validate() throws CruiseControlException {
        ValidationHelper.assertIsSet(username, "username", this.getClass());
        ValidationHelper.assertIsSet(password, "password", this.getClass());
	}
}