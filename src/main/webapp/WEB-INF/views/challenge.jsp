<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<tt:html>
	<tt:head title="Waisda uitdaging">
	</tt:head>
	<tt:body pageName="challenge">
		<!-- FRANK FIXME :-) -->
		<div class="box colorbox span8">
	 		<header class="header rich"><h2 class="h2">Welcome to Waisda<span class="boxline boxline-orange"></span></h2></header>
			<div class="box">
				<section>
				<h3>${game.initiator.name} plays Waisda? and has scored ${scoreToBeat} points by playing ${game.video.title}. Can you improve this score?</h3>
				<p>Instructions:</p>
				<ul>
					<li>Add as many words that describe what you see and what you hear</li>
					<li>Confirm your entry by pressing the [enter] key on your keyboard</li>
					<li>Scoring is based on matching entries from other players</li>
					<li>Some specific terms relevant to the content will result in bonus points</li>
					<li><a href="/voorwaarden">Terms and conditions</a> apply to playing the game</li>
				</ul>
				<p>Good luck!</p>
				<form method="post" id="challengeForm">
						<fieldset>
							<div class="form-actions center">
								<a href="#" onclick="document.getElementById('challengeForm').submit(); return false" class="btn btn-primary btn-large" tabindex="9">Accept challenge</a>
							</div>
			
							<input type="submit" value="Start" tabindex="10" style="position:absolute;top:0;left:-10000px;"/>
						</fieldset>  
				</form>
			</section>
			</div>
		</div>


		<!-- Over Waisda -->
<!-- 		<div class="colorbox box shadowbox span4 leading">
			<div class="shadowOverlay"></div>
			<header class="header rich">
				<h2 class="h2">Over Waisda small>last 7 days</small</h2><span class="bird bird-blue"></span><span class="boxline boxline-blue"></span>
			</header>
			<section class="box scroll-box fixed-low">
				<p>Het spel is ontwikkeld om het rijke archief van beeldmateriaal van Vroege Vogels beter toegankelijk te maken door op een speelse manier informatie te verzamelen over video's. Met jouw hulp worden deze video's beter vindbaar en verrijkt met aanvullende gegevens over de video.</p>
			</section>
		</div>
 -->




		
	</tt:body>
</tt:html>
