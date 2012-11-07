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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InfoController {

	@RequestMapping("/voorwaarden")
	public String voorwaarden(ModelMap modelMap) {
		modelMap.addAttribute("cssClass", "content");
		return "voorwaarden";
	}

	@RequestMapping("/over-het-spel")
	public String overHetSpel(ModelMap modelMap) {
		modelMap.addAttribute("cssClass", "content");
		return "overHetSpel";
	}

	@RequestMapping("/spelinstructies")
	public String hoeWerktHet(ModelMap modelMap) {
		modelMap.addAttribute("cssClass", "content");
		return "spelinstructies";
	}
}
