var EventBase = base2.Base.extend({
    evtHandles : [],

    addEvent : function(evtName, handle) {
        this.evtHandles.push({
            evtName : evtName,
            handle : handle
        });
    },

	dispatchEvents : function(evtName, params) {
		// loop through all event handles, match event names and fire
		for ( var i = 0; i < this.evtHandles.length; i++) {
		    var handler = this.evtHandles[i];
			if (handler.evtName == evtName) {
				if (params == null || params == undefined || params.length == 0) {
    			    handler.handle.apply(this); // IE 8 doesn't want params if no params present (or something)
				} else {
			        handler.handle.apply(this, params);
                }
			}
		}
	    return true;
	}


});

var NPOPlayer = EventBase.extend({
			constructor : function(elementId, config) {
				this.elementId = elementId;

				var seekTime = Math.max(0, config.startTime);
				var autoplay = config.startTime >= 0
						&& config.startTime < config.duration;

				this.config = {
					width : vidPlayerWidth,
					height : vidPlayerHeight,
					volume : '100',
					showBorder : 'no',
					embedEnabled : 'yes',
					controlBarEnabled : 'no',
					viewMode : 'video',
					playMode : autoplay ? 'play' : 'pause',
					playlistEnabled : 'no',
					playlistAdvanceEnabled : 'no',
					fragmentID : config.fragmentID,
					callbackFunction : 'ugCallbackFunction'
				};
				if (autoplay) {
					this.config.seekTime = Utils.unparseTime(seekTime);
				}

				this.player = new UGSLPlayer(document.getElementById(elementId),
						'http://embed.player.omroep.nl/slg/ugslplayer.xap',
						this.config);
			},

			// update variables
			onTick : function(elapsed, duration) {
				this.elapsed = Math.ceil(elapsed * 1000);
				this.duration = Math.ceil(duration * 1000);

				// check whether the videoplayer initialized completely
				if (this.duration != 0) {
					this
							.dispatchEvents("tick", [ this.elapsed,
									this.duration ]);
				}
			},

			getElapsed : function() {
				return this.elapsed;
			},

			moveTo : function(sec) {
				// this.player.sendEvent("SCRUB", sec);
			},

			play : function() {
				this.player.setConfigProperty('playMode', 'play');
			},

			stop : function() {
				this.player.setConfigProperty('playMode', 'pause');
			}
		});

var ugCallbackFunction = function(eventType, args) {
	if (eventType = 'PropertyChanged') {
//		console.log('property %s changed from %s to %s', args.name, args.oldvalue, args.newvalue);
		var vp = game.videoplayer;
		if (args.name == 'seekTime' || args.name == 'videoStatus') {
			var streamStatus = vp.player.getConfigProperty('videoStatus');
			if (streamStatus != 'playing') {
				return;
			}
			var seekTime = Utils.parseTime(vp.player
					.getConfigProperty('seekTime'));
			var startTime = Utils.parseTime(vp.player
					.getConfigProperty('startTime'));
			var endTime = Utils.parseTime(vp.player
					.getConfigProperty('endTime'));
			if (!startTime || !endTime) {
				return;
			}
			var duration = endTime - startTime;
			var elapsed = seekTime;
			if (elapsed > duration - 5) {
				vp.nearEnd = true;
			}
			vp.onTick(elapsed, duration);
		} else if (args.name == 'playMode' && args.newvalue == 'pause' && vp.nearEnd) {
			vp.dispatchEvents("fragmentEnd");
		}
	}
};

var onSilverlightLoad = function() {
	game.initVideoPlayer();
};

var JWPlayer = EventBase.extend({
	constructor : function(elementId, imageUrl, sourceUrl) {
		this.elementId = elementId;

		var self = this;
        jwplayer.key = svf.config.jwplayerKey;
		this.player = jwplayer(elementId).setup({
			controls: false,
			file: sourceUrl,
			image: imageUrl,
			height: 351,
			width: 618,
			events: {
				onComplete: function() {
				    self.dispatchEvents("fragmentEnd");
				    return true;
				},
				onTime: function() {
					var elapsed = Math.ceil(self.player.getPosition() * 1000);
					var duration = Math.ceil(self.player.getDuration() * 1000);

					// check whether the videoplayer initialized completely
					if (duration != 0) {
						self.dispatchEvents("tick", [ elapsed, duration ]);
					}
					return true;
				}
			}
		});
    },

	getElapsed : function() {
		return Math.ceil(this.player.getPosition() * 1000);
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

var VDPlayer = EventBase.extend({
	constructor : function(elementId, guci) {
		this.elementId = elementId;

		var self = this;

		this.player = svf(elementId).setup({
			height: 351,
			width: 618,
            type: 'guci',
            id: guci,
            sharer: false
		});
        this.player.onReady(function() {
            this.player.setControls(false);
        });
		this.player.onComplete(function() {
		    self.dispatchEvents("fragmentEnd");
		    return true;
		});
		this.player.onTime(function() {
            var elapsed = Math.ceil(self.player.getPosition() * 1000);
            var duration = Math.ceil(self.player.getDuration() * 1000);

            // check whether the videoplayer initialized completely
            if (duration != 0) {
                self.dispatchEvents("tick", [ elapsed, duration ]);
            }
            return true;
        });
    },

	getElapsed : function() {
		return Math.ceil(this.player.getPosition() * 1000);
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
