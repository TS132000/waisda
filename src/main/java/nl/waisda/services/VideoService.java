/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.waisda.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import nl.waisda.domain.Video;
import nl.waisda.model.Cache;
import nl.waisda.model.Channel;
import nl.waisda.model.Value;
import nl.waisda.repositories.VideoRepository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

	private static final String LOG_MSG_NUMBER_OF_VIDEOSETS_UNEQUAL_NUMBER_OF_CHANNELS = 
			"You have configured %s video sets while there are %s channels";

	private static final String LOG_MSG_LESS_VIDEOS_FOUND_THAN_REQUESTED = 
			"I got %s videos for set '%s' where %s was requested, so channels are not representing the videos as configured";

	private static final String LOG_MSG_NO_VIDEOSETS_FOR_CHANNELS_CONFIGURED = 
			"There are no video sets configured for the channels";

	private static final String LOG_MSG_NO_PREFIXES_ABOUT_ATTR_VALUE_CONFIGURED = 
			"There are no prefixes configured for the about attributes values";

	private static final String NOT_CONFIGURED = "!!! NOT CONFIGURED !!!";

	public static final long MAX_CHANNEL_AGE = 60000;

	private static Logger log = Logger.getLogger(VideoService.class);

	@Autowired
	private VideoRepository videoRepo;

	@Resource(name = "videoSetForEachChannel")
	private List<String> videoSetForEachChannel = Collections.emptyList();

	@Resource(name = "skosServiceUrlsPerVideoSet")
	Map<String, List<String>> skosServiceUrlsPerVideoSet;

	@Resource(name = "prefixesAboutTagPerSkosService")
	private Map<String, List<String>> prefixesAboutTagPerSkosService = Collections.emptyMap();

	private Value<List<Channel>> channelContent;

	private Map<String, List<Video>> getVideosPerVideoSet(List<String> channelVideoSets) {
		Map<String, Integer> videoSetCount = new HashMap<String, Integer>();
		for (String videoSet : channelVideoSets) {
			Integer count = videoSetCount.get(videoSet);
			videoSetCount.put(videoSet, (count == null) ? 1 : count + 1);
		}

		Map<String, List<Video>> videosPerSet = new HashMap<String, List<Video>>();
		for (String videoSet : videoSetCount.keySet()) {
			int wantedNumberOfVideos = videoSetCount.get(videoSet);
			List<Video> videos = videoRepo.getFeaturedVideosBySourceUrlStartWith(videoSet, wantedNumberOfVideos);
			if (videos.size() < wantedNumberOfVideos) {
				log.warn(String.format(LOG_MSG_LESS_VIDEOS_FOUND_THAN_REQUESTED, videos.size(), videoSet,
						wantedNumberOfVideos));
			}
			videosPerSet.put(videoSet, videos);
		}

		return videosPerSet;
	}

	private boolean areVideoSetsConfigured() {
		return videoSetForEachChannel != null || videoSetForEachChannel.size() > 0;
	}

	private boolean areSkosServiceUrlsPerVideoSetConfigured() {
		return skosServiceUrlsPerVideoSet != null || skosServiceUrlsPerVideoSet.size() > 0;
	}

	private boolean arePrefixesAboutTagPerSkosServiceConfigured() {
		return prefixesAboutTagPerSkosService != null || prefixesAboutTagPerSkosService.size() > 0;
	}

	private void validateConfiguredVideoSetForEachChannel() {
		if (!areVideoSetsConfigured()) {
			log.error(LOG_MSG_NO_VIDEOSETS_FOR_CHANNELS_CONFIGURED);
		} else if (areSkosServiceUrlsPerVideoSetConfigured() && !arePrefixesAboutTagPerSkosServiceConfigured()) {
			log.error(LOG_MSG_NO_PREFIXES_ABOUT_ATTR_VALUE_CONFIGURED);
		} else {
			if (VideoRepository.NCHANNELS != videoSetForEachChannel.size()) {
				log.warn(String.format(LOG_MSG_NUMBER_OF_VIDEOSETS_UNEQUAL_NUMBER_OF_CHANNELS,
						videoSetForEachChannel.size(), VideoRepository.NCHANNELS));
			}

			for (int channelNumber = 1; channelNumber <= VideoRepository.NCHANNELS; channelNumber++) {
				log.info("Channel: " + channelNumber);
				if (this.videoSetForEachChannel.size() >= channelNumber) {
					String videoSet = videoSetForEachChannel.get(channelNumber - 1);
					log.info("\tVideoSet: " + videoSet);
					List<String> skosServiceUrls = skosServiceUrlsPerVideoSet.get(videoSet);
					if (skosServiceUrls == null || skosServiceUrls.isEmpty()) {
						log.info("\t\tTable 'DictionaryEntry' will be used");
					} else {
						for (String skosServiceUrl : skosServiceUrls) {
							log.info("\t\tSkosServiceUrl: " + skosServiceUrl);
							List<String> prefixesAbout = prefixesAboutTagPerSkosService.get(skosServiceUrl);
							if (prefixesAbout == null || prefixesAbout.isEmpty()) {
								log.error("\t\t\tPrefixAbout: " + NOT_CONFIGURED);
							} else {
								for (String prefixAbout : prefixesAbout) {
									log.info("\t\t\tPrefixAbout: " + prefixAbout);
								}
							}
						}
					}
				} else {
					log.error("\tVideoSet: " + NOT_CONFIGURED);
				}
			}
		}
	}

	private List<Video> getVideosForChannels() {
		final List<Video> orderedVideos = new ArrayList<Video>();
		Map<String, List<Video>> videosPerVideoSet = getVideosPerVideoSet(videoSetForEachChannel);
		for (String videoSet : videoSetForEachChannel) {
			List<Video> videos = videosPerVideoSet.get(videoSet);
			if (!videos.isEmpty()) {
				orderedVideos.add(videos.get(0));
				videos.remove(0);
			}
		}
		return orderedVideos;
	}

	@PostConstruct
	public void init() throws Exception {
		validateConfiguredVideoSetForEachChannel();

		final List<Video> videosForChannels = getVideosForChannels();

		channelContent = new Value<List<Channel>>() {

			@Override
			public List<Channel> get() {
				List<Video> videos = videosForChannels;
				List<Channel> channels = new ArrayList<Channel>(videos.size());
				for (Video video : videos) {

					int highscore = videoRepo.getHighscore(video.getId());
					channels.add(new Channel(video, highscore));
				}
				return channels;
			}
		};

		channelContent = new Cache<List<Channel>>(channelContent, MAX_CHANNEL_AGE);
	}

	public Video getVideoById(int videoId) {
		return videoRepo.getById(videoId);
	}

	public List<Channel> getChannelContent() {
		return channelContent.get();
	}

}
