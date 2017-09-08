<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<tt:html>
	<tt:head title="Registreren">
	</tt:head>
	<tt:body pageName="register">
		<div class="box span9">
	 		<h1 class="h2 form-shift">Registreer</h1>
	 		<c:if test="${user.totalScore > 0}">
				<p  class="form-shift">Je <nf:format number="${user.totalScore}" /> punten worden opgeslagen nadat je een account hebt aangemaakt.</p>
			</c:if>			
			<f:form commandName="form" autocomplete="off" id="meedoenForm" class="form-horizontal">
	  		<fieldset>
 	  			    <c:set var="emailErrors"><f:errors path="email"/></c:set>
					<div class="control-group ${not empty emailErrors ? 'error' : ''}">
						<f:label path="email" cssClass="control-label">E-mailadres</f:label>
						<div class="controls" >
							<f:input path="email" tabindex="4" cssClass="input-xlarge" /><br/>
							<f:errors path="email" cssClass="help-inline"/>
						</div>
					</div>

 	  			    <c:set var="passwordErrors"><f:errors path="auth.password"/></c:set>
					<div class="control-group ${not empty passwordErrors ? 'error' : ''}">
						<f:label path="auth.password" cssClass="control-label">Wachtwoord</f:label>
						<div class="controls">
		  				<f:password path="auth.password" tabindex="5" cssClass="input-xlarge" /><br/>
		  				<f:errors path="auth.password" cssClass="help-inline"/>
		  			</div>
		  		</div>

  			    <c:set var="repeatPasswordErrors"><f:errors path="auth.repeatPassword"/></c:set>
		  		<div class="control-group ${not empty repeatPasswordErrors ? 'error' : ''}">
						<f:label path="auth.repeatPassword" cssClass="control-label">Herhaal wachtwoord</f:label>
						<div class="controls">
							<f:password path="auth.repeatPassword" tabindex="6" cssClass="input-xlarge" /><br/>
		  				<f:errors path="auth.repeatPassword" cssClass="help-inline"/>
		  			</div>
		  		</div>

	  			    <c:set var="nameErrors"><f:errors path="auth.name"/></c:set>
					<div class="control-group ${not empty nameErrors ? 'error' : ''}">
						<f:label path="auth.name" cssClass="control-label">Gebruikersnaam</f:label>
						<div class="controls">
		  				<f:input path="auth.name" tabindex="7" cssClass="input-xlarge" /><br/>
		  				<f:errors path="auth.name" cssClass="help-inline"/>
		  				<p class="help-block">Je gebruikersnaam is zichtbaar voor andere spelers. Na registratie kun je hem niet meer wijzigen.</p>
					</div>
				</fieldset>
				<fieldset>
	  			    <c:set var="agreeTosErrors"><f:errors path="agreeTos"/></c:set>
					<div class="control-group ${not empty agreeTosErrors ? 'error' : ''}">
						<div class="controls">
							<label for="agree_tos" class="checkbox">
								<f:checkbox value="" path="agreeTos" id="agree_tos" tabindex="8"/>Ik ga akkoord met de <a href="voorwaarden" target="_blank">voorwaarden</a>
							</label>
							<f:errors path="agreeTos" cssClass="help-inline"/>
						</div>
					</div>
				

					<div class="form-actions">
    					<input type="submit" value="Registreer" class="btn btn-primary btn-large" tabindex="10"/>
					</div>
	
				</fieldset>
			</f:form>
		</div>
	</tt:body>
</tt:html>
