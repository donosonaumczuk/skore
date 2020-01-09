package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.exceptions.SportNotFoundException;
import ar.edu.itba.paw.interfaces.SportDao;
import ar.edu.itba.paw.interfaces.SportService;
import ar.edu.itba.paw.models.Sport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SportServiceImpl.class);

    @Autowired
    private SportDao sportDao;

    @Override
    public Optional<Sport> findByName(final String sportName) {
        return sportDao.findByName(sportName);
    }


    @Override
    public Sport create(final String sportName, final int playerQuantity, final String displayName,
                        final MultipartFile file) throws IOException {
        Optional<Sport> sport = sportDao.create(sportName, playerQuantity, displayName, file);

        return sport.orElseThrow(() -> new SportNotFoundException("Can't find sport with name: " + sportName));
    }

    @Override
    public Sport modifySport(final String sportName, final String displayName,
                             final MultipartFile file) throws IOException {

        Optional<Sport> sport = sportDao.modifySport(sportName, displayName, file);
        return sport.orElseThrow(() -> new SportNotFoundException("can't found sport with name: " + sportName));
    }

    @Override
    public boolean remove(final String sportName) {
        return sportDao.remove(sportName);
    }


    @Override
    public List<Sport> getAllSports() {
        return sportDao.getAllSports();
    }

    @Override
    public byte[] readImage(final String sportName) {
        Optional<byte[]> imagesOpt = sportDao.readImage(sportName);

        return imagesOpt.orElseThrow(() -> new ImageNotFoundException("Fail to read image from " + sportName));
    }
}
