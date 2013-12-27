var TaggingHistory = base2.Base.extend({
	
	idPrefix : 'tag-',
	
	update: function(tags, currentTime) {
		$.each(tags, $.proxy(function(i, tag){
			var el = $('#' + this.idPrefix + tag.id);

			if (!el.length) { // if (!el.length) {
				// Create an element for it
				el = $('<div/>', { 'class' : 'tag', 'id' : this.idPrefix + tag.id });
				var match = false;
				if (tag.matchingTag) {
					el.addClass('match');
					match = true;
				}

				el.append(document.createTextNode(tag.tag));

				var explanation;
				if (tag.pioneer && tag.matchingTag != null ) {
					if (match) {
						explanation = texts.pionier;
					}
					appendIcon(el, "pionier", explanation, true, match);
				}

				if (tag.pioneer && tag.matchingTag && tag.matchingTag == tag.tag) {
					appendIcon(el, "+75", texts.pionier, false, match);
				} else if (tag.matchingTag && tag.matchingTag != tag.tag) {
					appendIcon(el, "+25", texts.hierarchie, false, match);
				} else if (tag.matchingTag && tag.matchingTag == tag.tag) {
					appendIcon(el, "+50", texts.match, false, match);
				}

				el.append($('<span/>', { 'class' : 'tag', 'class' : 'points', 'text': '+' + tag.score }));
				
			} else {
				// Update existing element
				$('span.points', el).text('+' + tag.score);
				if (tag.matchingTag && tag.pioneer) {
					// Add a pioneer icon if one doesn't exist yet
					var createNew = true;
					$('span', el).each(function(i, span) {
						if (span.hasClass("pionier")) {
							createNew = false;
						}
					});
					if (createNew) {
						el.append($('<span/>', { 'class' : 'icon pionier', 'text': 'pionier' }));
					}
				}
			}

			$('#tagList').prepend(el);
		}, this));
	}

	

});

$(function(){
	var tags = $("#tagList > .tag");
	console.log("taglist", tags);
	$.each(tags, function(t, tag){
		$('.explanation-bubble').addClass('hidden').html("");
		$(tag).hover(function(){	
			console.log("hovert");
			var t = $(this);
			var icons = t.find(".icon");
			var isMatch = t.hasClass('match');
			var isPionier = t.hasClass('pionier');
			var isHierarchy = t.hasClass('hierarchy');
			var isConfirmed = t.hasClass('confirmed');

			// put explanation texts in the bubble for each icon
			$('.explanation-bubble').addClass('hidden').html("");
			$.each(icons, function(i, icon){
				var prettyName = $(icon).html();
				//fillExplanationBubble(prettyName, lookupExplanationText(prettyName, isMatch), true, isMatch);
			});
			if (isHierarchy) {
				fillExplanationBubble("+25", texts.hierarchie, false, isMatch);
			} else if (isConfirmed || isPionier) {
				fillExplanationBubble("+50", texts.match, false, isMatch);
			}
			positionBubble($(tag));
		}, function(){
			$('.explanation-bubble').addClass('hidden').html("");
		});


	});


});

function lookupExplanationText(prettyName, isMatch) {
	
	if (prettyName == "pionier") {
		return texts.pionier;
	};
	if (prettyName == "match") {
		return isMatch ? texts.locatie_behaald : texts.locatie_potentieel;
	};
	if (prettyName == "hyrarchie") {
		return isMatch ? texts.locatie_behaald : texts.locatie_potentieel;
	};
}

function appendIcon(el, prettyName, explanation, drawIcon, isMatch) {
	if (drawIcon) {
		el.append($('<span/>', { 'class' : 'icon ' + prettyName, 'text': prettyName }));
	}
	el.hover(function(){
		fillExplanationBubble(prettyName, explanation, drawIcon, isMatch);
		positionBubble(el);
	}, function(){
		$('.explanation-bubble').addClass('hidden').html("");
	});
	
}

function fillExplanationBubble(prettyName, explanation, drawIcon, isMatch) {
	var bubble = $('.explanation-bubble');
	var bubbleRow = $("<div class='explanationRow' />"); // hier moet nog een match op als deze een match is
	if (isMatch) bubbleRow.addClass('match');
	var icon = $("<div class='leftCol'><span class='icon " + prettyName + "'>" + prettyName + "</span></div>");
	if (!drawIcon) icon.addClass('numberIcon');
	bubbleRow.append(icon);
	bubbleRow.append("<span class='explanation'>" + explanation + "</span>");
	bubble.append(bubbleRow).removeClass("hidden");

}

function positionBubble(tag) {
	var tagListParent = $("#tagList").parent();
	tagListParent.scroll(function(){ // remove bubble if tag list is scrolled
		$('.explanation-bubble').addClass('hidden').html("");
	});

	var tagPosition = tag.position();
	var tagListParentScrollTop = tagListParent.scrollTop();

	var bubble = $('.explanation-bubble');
	var bubbleHeight = bubble.height();
	bubble.css("top", bubbleHeight * -1 + tagPosition.top - tagListParentScrollTop - 60);


}

var texts = {
	pionier : "You were the first to introduce this tag",
	match : "This tag was confirmed by another player",
	hierarchie : "This tag is related to a tag from another player"
};
