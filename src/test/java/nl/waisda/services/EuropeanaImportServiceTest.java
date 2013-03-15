package nl.waisda.services;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import nl.waisda.domain.PlayerType;
import nl.waisda.domain.Video;
import nl.waisda.repositories.VideoRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 8-3-13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/nl/waisda/services/europeanaImportService-test.xml")
//@Transactional
public class EuropeanaImportServiceTest {
//http://preview.europeana.eu/api/v2/search.json?wskey=xxxxxxxx&query=provider_aggregation_edm_dataProvider%3A%22Open+Beelden%22&start=1&rows=12&profile=standard
    @Autowired
    private EuropeanaImportServiceIF service;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Test
    //@Transactional
    //@Rollback(false)
    public void testImport() {
        try {
            //service.importEuropeanaData("provider_aggregation_edm_dataProvider%3A%22Open+Beelden%22");
            service.runDetached("Open Beelden");
            while (service.isRunning()) {
                Thread.sleep(5000);
            }
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("Shouldn't fail");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    @Ignore
    public void testInsert() {
        Video video = new Video();

        video.setDuration(0);
        video.setEnabled(true);
        video.setFragmentID(null);
        video.setImageUrl("http://www.image.url");
        video.setPlayerType(PlayerType.JW);
        video.setSourceUrl("http://www.video.url");
        video.setStartTime(0);
        video.setTitle("My big fat title");
        EntityManagerFactory o = entityManagerFactory.getObject();
        o.createEntityManager().getTransaction();
        videoRepository.store(video);

    }
}
