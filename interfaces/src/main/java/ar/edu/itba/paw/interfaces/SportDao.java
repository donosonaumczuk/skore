package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Sport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SportDao {

    public Optional<Sport> findByName(final String sportName);

    public Optional<Sport> create(final String sportName, final int playerQuantity, final String displayName,
                                  final byte[] file);

    public Optional<Sport> modifySport(final String sportName, final String displayName,
                                                final byte[] file);

    public boolean remove(final String sportName);

    public List<Sport> getAllSports();

    public Optional<byte[]> readImage(final String sportName);

}
