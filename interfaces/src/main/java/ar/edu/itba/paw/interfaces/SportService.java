package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Sport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SportService {
    @Transactional
    public Optional<Sport> findByName(final String sportName);

    @Transactional
    public Optional<Sport> create(final String sportName, final int playerQuantity,
                        final String displayName, final byte[] file);

    @Transactional
    public Optional<Sport> modifySport(final String sportName, final String displayName,
                             final byte[] file);

    @Transactional
    public boolean remove(final String sportName);

    @Transactional
    public List<Sport> getAllSports();

    @Transactional
    public Optional<byte[]> readImage(final String sportName);
}
