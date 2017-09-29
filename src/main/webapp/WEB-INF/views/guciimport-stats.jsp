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
			<f:form commandName="form" class="admin-width-100pct" enctype="multipart/form-data" method="POST">
			    <f:errors cssClass="errors"/>
				<c:choose>
				    <c:when test="${form.importRunning == false || empty form.importRunning}">
				        <h1>Upload file</h1>
                        <fieldset class="admin-width-600px">
                            <div class="control-group">
                                <label for="file" cssClass="admin-label-200px">CSV File</label>
                                <div class="admin-value-300px">
                                    <input id="file" type="file" name="file" cssClass="admin-input-300px"/>
                                </div>
                            </div>
                        </fieldset>
                        <input type="submit" name="start" value="Start"/>
				    </c:when>
				    <c:otherwise>
                        <h1>Current stats</h1>
                        <fieldset class="admin-width-600px">
                            <script>
                                window.setTimeout(function() {
                                    $('#admin-body').load('./guci/stats');
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