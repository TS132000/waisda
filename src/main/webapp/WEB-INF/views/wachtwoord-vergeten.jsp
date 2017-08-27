<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>

<%@ taglib tagdir="/WEB-INF/tags" prefix="tt"%>

<tt:html>
<tt:head title="Password forgotten" />
<tt:body pageName="wachtwoord-vergeten">

	<div class="box span9">
	<h1 class="h2 form-shift">Bent je jouw wachtwoord vergeten?</h1>
	<c:choose>
	<c:when test="${form.user == null}">
		<p class="help-block form-shift">Als je jouw wachtwoord bent vergeten kun je het onderstaande formulier invullen.
			Je ontvangt dan een email met instructies over hoe je een nieuw wachtwoord kunt instellen.</p>
		<f:form commandName="form" action="/wachtwoord-vergeten" id="form"
			class="form-horizontal" method="post">
			<fieldset>
				<div class="control-group">
					<f:label path="email" class="control-label">Email addres</f:label>
					<div class="controls">
						<f:input path="email" id="email" /><br/>
						<f:errors path="*" cssClass="help-inline" />
					</div>
				</div>		
				<div class="form-actions">
					<a href="#"
					onclick="document.getElementById('form').submit(); return false"
					id="submitLogin" class="btn btn-primary btn-large">Vraag nieuw wachtwoord aan</a> <input
					type="submit" value="Inloggen in mijn account"
					style="position: absolute; top: 0; left: -10000px;" />
				</div>
			</fieldset>
		</f:form>
	</c:when>
	<c:otherwise>
		<p class="help-block form-shift">Je ontvangt over enkele minuten een email met de instructies.
		<br/>
		<br/>
		<a href="/">Naar de Hoofdpagina</a></p>
	</c:otherwise>
	</c:choose>
	</div>

</tt:body>
<script type="text/javascript">
	$('#email').focus();
	</script>
</tt:html>