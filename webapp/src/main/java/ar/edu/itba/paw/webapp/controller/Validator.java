package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PremiumUserService;
import ar.edu.itba.paw.webapp.exceptions.ApiException;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

    private static Validator validator;

    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList(MediaType.IMAGE_JPEG_VALUE,
            "image/jpg", MediaType.IMAGE_PNG_VALUE);
    private static final int MEGABYTE = 1024 * 1024;

    @Autowired
    @Qualifier("premiumUserServiceImpl")
    private PremiumUserService premiumUserService;

    private Validator() {
        //Singleton
    }

    public static Validator getValidator() {
        if (validator == null) {
            validator = new Validator();
        }
        return validator;
    }

    public Validator userExist(final String username) {
        premiumUserService.findByUserName(username)
            .orElseThrow(() -> {
                LOGGER.trace("Can't get '{}', user not found", username);
                return new ApiException(HttpStatus.NOT_FOUND, "User '" + username + "' does not exist");
            });
        return this;
    }

    public Validator fieldHasData(final String field, final String fieldName) {
        if(field == null || field.isEmpty()) {
            LOGGER.trace("No data in field '{}'", fieldName);
            throw new ApiException(HttpStatus.BAD_REQUEST, "No data in field '" + fieldName + "'");
        }
        return this;
    }

    public byte[] validateAndProcessImage(final String imageInBase64) {
        if(imageInBase64 == null) {
            return null;
        }

        fieldHasData(imageInBase64, "image");
        String[] splitImageBase64 = imageInBase64.split(",");
        splitimageHasBase64Format(splitImageBase64);

        String mimeType = splitImageBase64[0].substring(5, splitImageBase64[0].indexOf(';'));
        byte[] imageBytes = getImageBytes(splitImageBase64[1]);
        String mimeTypeFromBytes  = getMimeTypeFromBytes(imageBytes);

        isMimeTypeSupported(mimeTypeFromBytes);

        if(!mimeType.equals(mimeTypeFromBytes)) {
            LOGGER.trace("Mismatch header mime-type with data mime-type");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Mismatch header mime-type with data mime-type");
        }

        if(imageBytes.length > MEGABYTE) {
            LOGGER.trace("Image is bigger than {} bytes", MEGABYTE);
            throw new ApiException(HttpStatus.BAD_REQUEST, "Image is bigger than " + MEGABYTE + " bytes");
        }
        return imageBytes;
    }

    public Validator isAlphaNumericAndLessThan(final String string, final String fieldName, final int maxsize) {
        if (string == null || !string.matches("[a-zA-Z0-9]+") || string.length() > maxsize || string.isEmpty()) {
            LOGGER.trace("The field '{}' must be alphanumeic and less than {} characters", fieldName, maxsize);
            throw new ApiException(HttpStatus.BAD_REQUEST, "The field '" + fieldName + "' must be alphanumeic and less than"
                    + maxsize + " characters");
        }
        return this;
    }

    public Validator isNumberGreaterThanZero(final int number, final String fieldName) {
        if(number < 0) {
            LOGGER.trace("The field '{}' must be a integer number greater than zero", fieldName);
            throw new ApiException(HttpStatus.BAD_REQUEST, "The field '" + fieldName + "' must be integer number " +
                    "greater than zero");
        }
        return this;
    }

    private Validator isMimeTypeSupported(final String mimeType) {
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
            throw new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exceptionError.toString());

        }
        return this;
    }

    private Validator splitimageHasBase64Format(final String[] splitImage) {
        if (splitImage.length != 2 || !splitImage[0].matches("data:image/(\\w+);base64")) {
            LOGGER.trace("Image is not in base64 format");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Image must be in base64 format");
        }
        return this;
    }

    private byte[] getImageBytes(final String imageDataBase64) {
        try {
            return DatatypeConverter.parseBase64Binary(imageDataBase64);
        } catch (IllegalArgumentException e) {
            LOGGER.trace("Cannot process image bytes");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid image bytes");
        }
    }

    private String getMimeTypeFromBytes(byte[] imageBytes) {
        try {
            return getMimeType(imageBytes);
        } catch (IOException | TikaException | SAXException e) {
            LOGGER.error("Error processing image mime-type");
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image mime-type");
        }
    }

    private String getMimeType(byte[] imageBytes) throws IOException, TikaException, SAXException {
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

        String mimeType = metadata.get(HttpHeaders.CONTENT_TYPE);
        return mimeType;
    }
}
