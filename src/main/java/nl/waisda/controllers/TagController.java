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

package nl.waisda.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import nl.waisda.domain.TagEntry;
import nl.waisda.domain.Video;
import nl.waisda.exceptions.NotFoundException;
import nl.waisda.model.TagEntryStats;
import nl.waisda.model.VideoStats;
import nl.waisda.repositories.TagEntryRepository;
import nl.waisda.repositories.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TagController {

	@Autowired
	private TagEntryRepository tagEntryRepo;

	@Autowired
	private VideoRepository videoRepo;

	@RequestMapping(value = "/tag/{tag}", method = RequestMethod.GET)
	public String profile(@PathVariable String tag, ModelMap model,
			HttpSession session) throws NotFoundException {
		String normalizedTag = TagEntry.normalize(tag);
		List<Video> videos = videoRepo.getTopVideosForTag(normalizedTag, 6);

		TagEntryStats stats = new TagEntryStats();
		stats.setNormalizedTag(normalizedTag);
		stats.setFirstEntry(tagEntryRepo.getFirstEntry(normalizedTag));

		for (Video v : videos) {
			VideoStats vs = new VideoStats(v,
					tagEntryRepo.getTopTags(v.getId(), 5));
			stats.getVideoStats().add(vs);
		}

		model.put("tagEntryStats", stats);
		return "tag";
	}
}
