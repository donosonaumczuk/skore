package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImageValidators {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageValidators.class);

    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList(MediaType.IMAGE_JPEG_VALUE,
            "image/jpg", MediaType.IMAGE_PNG_VALUE);
    private static final int MEGABYTE = 1024 * 1024;

    public static byte[] validateAndProcessImage(final String imageInBase64) {
        if(imageInBase64 == null) {
            return null;
        }

        fieldHasData(imageInBase64);
        String[] splitImageBase64 = imageInBase64.split(",");
        splitimageHasBase64Format(splitImageBase64);

        String mimeType = splitImageBase64[0].substring(5, splitImageBase64[0].indexOf(';'));
        byte[] imageBytes = getImageBytes(splitImageBase64[1]);
        String mimeTypeFromBytes  = getMimeTypeFromBytes(imageBytes);

        isMimeTypeSupported(mimeTypeFromBytes);

        if(!mimeType.equals(mimeTypeFromBytes)) {
            LOGGER.trace("Mismatch header mime-type with data mime-type");
            throw ApiException.of(HttpStatus.BAD_REQUEST, "Mismatch header mime-type with data mime-type");
        }

        if(imageBytes.length > MEGABYTE) {
            LOGGER.trace("Image is bigger than {} bytes", MEGABYTE);
            throw ApiException.of(HttpStatus.BAD_REQUEST, "Image is bigger than " + MEGABYTE + " bytes");
        }
        return imageBytes;
    }

    private static void fieldHasData(final String field) {
        if(field == null || field.isEmpty()) {
            LOGGER.trace("No data in field '{}'", "image");
            throw ApiException.of(HttpStatus.BAD_REQUEST, "No data in field '" + "image" + "'");
        }
    }

    private static void isMimeTypeSupported(final String mimeType) {
        if(mimeType == null || !SUPPORTED_MIME_TYPES.contains(mimeType)) {
            StringBuilder exceptionError = new StringBuilder("Media type not supported. The supported media types are: ");
            boolean isFirst = true;
            for (String mediatype: SUPPORTED_MIME_TYPES) {
                if (isFirst) {
                    isFirst = false;
                }
                else {
                    exceptionError.append(", ");
                }
                exceptionError.append(mediatype);
            }
            throw ApiException.of(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exceptionError.toString());

        }
    }

    private static void splitimageHasBase64Format(final String[] splitImage) {
        if (splitImage.length != 2 || !splitImage[0].matches("data:image/(\\w+);base64")) {
            LOGGER.trace("Image is not in base64 format");
            throw ApiException.of(HttpStatus.BAD_REQUEST, "Image must be in base64 format");
        }
    }

    private static byte[] getImageBytes(final String imageDataBase64) {
        try {
            return DatatypeConverter.parseBase64Binary(imageDataBase64);
        } catch (IllegalArgumentException e) {
            LOGGER.trace("Cannot process image bytes");
            throw ApiException.of(HttpStatus.BAD_REQUEST, "Invalid image bytes");
        }
    }

    private static String getMimeTypeFromBytes(byte[] imageBytes) {
        try {
            return getMimeType(imageBytes);
        } catch (IOException | TikaException | SAXException e) {
            LOGGER.error("Error processing image mime-type");
            throw ApiException.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image mime-type");
        }
    }

    private static String getMimeType(byte[] imageBytes) throws IOException, TikaException, SAXException {
        File file = File.createTempFile("is2f", ".tmp");
        file.deleteOnExit();
        FileOutputStream outStream = new FileOutputStream(file);
        IOUtils.copy(new BufferedInputStream(new ByteArrayInputStream(imageBytes)), outStream);

        AutoDetectParser parser = new AutoDetectParser();
        parser.setParsers(new HashMap<>());
        Metadata metadata = new Metadata();
        metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());

        InputStream stream = new FileInputStream(file);
        parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
        stream.close();

        return metadata.get(HttpHeaders.CONTENT_TYPE);
    }
}
