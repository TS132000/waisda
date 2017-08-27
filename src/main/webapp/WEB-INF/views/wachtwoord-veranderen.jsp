<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>

<tt:html>
<tt:head title="Wachtwoord veranderen">
</tt:head>
<tt:body pageName="wachtwoord-veranderen">

	<div class="box span9">
		<h1 class="h2 form-shift">Wachtwoord wijzigen</h1>
			<c:choose>
			<c:when test="${success}">
				<p class="help-block form-shift">Je wachtwoord is gewijzigd. <a style="color:#e00034;" href="/inloggen">Log opnieuw in</a> met je nieuwe wachtwoord.</p>
			</c:when>
			<c:when test="${errorMessage != null}">
				<div class="error-block">${errorMessage}</div>
			</c:when>
			<c:otherwise>
				<f:form commandName="form" action="/wachtwoord-veranderen" id="form" class="form-horizontal" method="post">
					<fieldset>
						<f:errors path="*" cssClass="error-block" />
						<c:if test="${form != null}">
							<p class="help-block form-shift spaced-min">De link in de email maakt het mogelijk je wachtwoord te wijzigen:</p>
							<input type="hidden" value="${form.id}" id="id" name="id" />
							<input type="hidden" value="${form.plainTextKey}" id="plainTextKey" name="plainTextKey"/>
							<div class="control-group">
								<f:label path="auth.password" cssClass="control-label">Nieuw wachtwoord</f:label>
								<div class="controls">
									<f:password path="auth.password" id="password" />
								</div>
							</div>
							<div class="control-group">
								<f:label path="auth.repeatPassword" cssClass="control-label">Herhaal nieuw wachtwoord</f:label>
								<div class="controls">
									<f:password path="auth.repeatPassword" id="repeatPassword" />
								</div>
							</div>
							
							<div class="form-actions">
								<a href="#"
									onclick="document.getElementById('form').submit(); return false"
									id="submitLogin" class="btn btn-primary btn-large">Opslaan</a> <input type="submit"
									value="Verander mijn wachtwoord"
									style="position: absolute; top: 0; left: -10000px;" />
							</div>
						</c:if>
					</fieldset>
				</f:form>
			</c:otherwise>
			</c:choose>

	</div>


</tt:body>
	<script type="text/javascript">
	$('#password').focus();
	</script>
</tt:html>