package nl.waisda.interceptors;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Pushes per user statics onto the session
 * @author Danny Sedney sdengineering77@gmail.com
 */
@Component
public class StaticModelInterceptor extends HandlerInterceptorAdapter {

    private static final String STATIC_MODEL_ATTR_NAME = "staticModel";

    // STATIC FIELDS NEED GETTERS WHEN USING BEANUTILS TO POPULATE
    @Value("${waisda.socialreference.title}")
    private String socialReferenceTitle;
    @Value("${waisda.socialreference.homeurl}")
    private String socialReferenceHome;
    @Value("${waisda.socialreference.description}")
    private String socialReferenceDescription;

    // this object must be constructed and pushed onto the session
    // when not there yet. It, ofcourse, may contain user specific data
    // and therefor is constructed once per session
    public class StaticModel implements Serializable {
        private static final long serialVersionUID = 1l;

        private String socialReferenceTitle;
        private String socialReferenceHome;
        private String socialReferenceDescription;

        public String getSocialReferenceTitle() {
            return socialReferenceTitle;
        }

        public void setSocialReferenceTitle(String socialReferenceTitle) {
            this.socialReferenceTitle = socialReferenceTitle;
        }

        public String getSocialReferenceHome() {
            return socialReferenceHome;
        }

        public void setSocialReferenceHome(String socialReferenceHome) {
            this.socialReferenceHome = socialReferenceHome;
        }

        public String getSocialReferenceDescription() {
            return socialReferenceDescription;
        }

        public void setSocialReferenceDescription(String socialReferenceDescription) {
            this.socialReferenceDescription = socialReferenceDescription;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(STATIC_MODEL_ATTR_NAME) == null) {
            StaticModel staticModel = new StaticModel();
            BeanUtils.copyProperties(this, staticModel);
            session.setAttribute(STATIC_MODEL_ATTR_NAME, staticModel);
        }

        return super.preHandle(request, response, handler);
    }

    public String getSocialReferenceTitle() {
        return socialReferenceTitle;
    }

    public String getSocialReferenceHome() {
        return socialReferenceHome;
    }

    public String getSocialReferenceDescription() {
        return socialReferenceDescription;
    }


}
