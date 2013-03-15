<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nf" uri="/WEB-INF/tld/NumberFormat.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tt" %>

<div class="row equal-cols">

	<div class="admin-width-100pct">

		<section class="reset relative">
			<f:form commandName="form" class="admin-width-100pct">
			    <f:errors cssClass="errors"/>
			    <f:hidden path="importSummary"/>
			    <f:hidden path="prevImportRequestString"/>
				<c:choose>
				    <c:when test="${form.importRunning == false || empty form.importRunning}">
				        <h1>Import query</h1>
                        <fieldset class="admin-width-600px">
                            <div class="control-group">
                                <f:label path="importRequestString" cssClass="admin-label-200px">Importer search query</f:label>
                                <div class="admin-value-300px">
                                    <f:input path="importRequestString" cssClass="admin-input-300px"/>
                                    <f:errors path="importRequestString" cssClass="errors"/>
                                </div>
                            </div>
                        </fieldset>
                        <c:if test="${not empty form.importSummary}">
                            <div class="admin-notify-600px">
                                About to import <i><c:out value="${form.importSummary}"/></i> items. Click the start button again to start import or type a new search query otherwise.
                            </div>
                        </c:if>
                        <input type="submit" name="start" value="Start"/>
				    </c:when>
				    <c:otherwise>
                        <h1>Current stats</h1>
                        <fieldset class="admin-width-600px">
                            <script>
                                window.setTimeout(function() {
                                    //window.location.reload();
                                    $('#admin-body').load('./stats');
                                }, 4000);
                            </script>
                            <div class="control-group">
                                <f:label path="importingTitle" cssClass="admin-label-200px">Current importing title</f:label>
                                <div class="admin-value-300px">
                                    <c:out escapeXml="true" value="${form.importingTitle}"/>
                                </div>
                            </div>
                            <div class="control-group">
                                <f:label path="importingProgress" cssClass="admin-label-200px">Current import item number</f:label>
                                <div class="admin-value-300px">
                                    <c:out escapeXml="true" value="${form.importingProgress}"/>
                                </div>
                            </div>
                            <div class="control-group">
                                <f:label path="importingTotalItems" cssClass="admin-label-200px">Current import num items</f:label>
                                <div class="admin-value-300px">
                                    <c:out escapeXml="true" value="${form.importingTotalItems}"/>
                                </div>
                            </div>
                        </fieldset>
                        <input type="submit" name="stop" value="Stop"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${not empty form.log}">
                    <h1>Progress Log</h1>
                    <div class="admin-log">
                        <c:forEach var="item" items="${form.log}">
                            <div>
                                <c:out escapeXml="false" value="${item}"/>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
			</f:form>
		</section>
	</div>
</div>