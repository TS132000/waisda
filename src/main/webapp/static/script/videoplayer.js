var NPOPlayer = base2.Base.extend({
			constructor : function(elementId, config) {
				var self = this;

				this.evtHandles = [];
				this.elementId = elementId;

				var player = npoplayer("video");
				this.player = player;
				var setupArg = {
						width: '618',
						height: '348',
						image: config.imageUrl,
						prid: config.prid,
						skipAdaptive: true,
						options: {
							title: config.title,
							autoPlay: true,
							allowSubtitles: false,
							startAt: config.startTimeWithinEpisode,
							endAt: config.startTimeWithinEpisode + config.duration,
							showTitlebar: false,
							showControlbar: false
						}
					};
				
				player.setup(setupArg);
				player.playerReady(function() {
					player.onComplete(function() { self.dispatchEvents("fragmentEnd"); });
					player.onTime(function() {
						var elapsed = Math.ceil(self.player.getPosition() * 1000);
						self.dispatchEvents("tick", [ elapsed, config.duration * 1000 ]);
					});
					player.onPause(function(){
						$(".pauze").addClass("play");
						$(".pauze").text("verder spelen");
						$("#inputField").focus();
						pause = true;
					});
					player.onPlay(function(){
						$(".pauze").removeClass("play");
						$(".pauze").text("pauze");
						$("#inputField").focus();
					});
				});
				
				$(".pauze").click(function(){
					if (self.player.getState() === "PAUSED") {
						$(".pauze").removeClass("play");
						$(".pauze").text("pauze");
						self.play();
					} else {
						$(".pauze").addClass("play");
						$(".pauze").text("verder spelen");
						self.stop();
					}
					
				});
			},

			getElapsed : function() {
				return Math.ceil(this.player.getPosition() * 1000);
			},

			addEvent : function(evtName, handle) {
				this.evtHandles.push({
					evtName : evtName,
					handle : handle
				});
			},

			dispatchEvents : function(evtName, params) {
				// loop through all event handles, match event names and fire
				for ( var i = 0; i < this.evtHandles.length; i++) {
					if (this.evtHandles[i].evtName == evtName) {
						this.evtHandles[i].handle.apply(this, params);
					}
				}
			},

			moveTo : function(sec) {
				this.player.seek(sec);
			},

			play : function() {
				this.player.play(true);
			},

			stop : function() {
				this.player.pause(true);
			}
		});

var JWPlayer = base2.Base.extend({
	constructor : function(elementId, imageUrl, sourceUrl) {
		this.evtHandles = [];
		this.elementId = elementId;

		var self = this;
		var pause = false;
		this.player = jwplayer(elementId).setup({
			flashplayer: '/static/mediaplayer-6.7/jwplayer.flash.swf',
			file: sourceUrl,
			image: imageUrl,
			height: 351,
			width: 618,
			events: {
				onComplete: function() { self.dispatchEvents("fragmentEnd"); },
				onTime: function() {
					var elapsed = Math.ceil(self.player.getPosition() * 1000);
					var duration = Math.ceil(self.player.getDuration() * 1000);

					// check whether the videoplayer initialized completely
					if (duration != 0) {
						self.dispatchEvents("tick", [ elapsed, duration ]);
					}
				},
				onPause: function(){
					$(".pauze").addClass("play");
					$(".pauze").text("verder spelen");
					pause = true;
				},
				onPlay: function(){
					$(".pauze").removeClass("play");
					$(".pauze").text("pauze");
				}
			}
		});
		
		$(".pauze").click(function(){
			if (pause) {
				$(".pauze").removeClass("play");
				$(".pauze").text("pauze");
				self.play();
				pause = false;
				
			} else {
				$(".pauze").addClass("play");
				$(".pauze").text("verder spelen");
				self.stop();
				pause = true;
			}
			
		});
	},

	getElapsed : function() {
		return Math.ceil(this.player.getPosition() * 1000);
	},

	addEvent : function(evtName, handle) {
		this.evtHandles.push({
			evtName : evtName,
			handle : handle
		});
	},

	dispatchEvents : function(evtName, params) {
		// loop through all event handles, match event names and fire
		for ( var i = 0; i < this.evtHandles.length; i++) {
			if (this.evtHandles[i].evtName == evtName) {
				this.evtHandles[i].handle.apply(this, params);
			}
		}
	},

	moveTo : function(sec) {
		this.player.seek(sec);
	},

	play : function() {
		this.player.play(true);
	},

	stop : function() {
		this.player.pause(true);
	}
});
