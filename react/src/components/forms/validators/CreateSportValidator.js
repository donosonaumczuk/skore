import i18next from 'i18next';
import { hasStringValidSymbols, isStringNumeric, 
         isStringAlphaNumericOrSpaces} from './utils/StringValidators';
import { validateRequiredField } from './utils/RequiredFieldValidators';
import { isStringBetweenValues } from './utils/NumericValidators';
import { validImageFormats } from './utils/ImageValidators';

const ERROR_BASE_LABEL = "createSportForm.errors.";

//sport name constants
const MIN_SPORTNAME_LENGTH = 4;
const MAX_SPORTNAME_LENGTH = 100;
const sportNameInvalidSymbols= /[^a-zA-Z0-9_]/;

//display name constants
const MIN_DISPLAY_NAME_LENGTH = 4;
const MAX_DISPLAY_NAME_LENGTH = 100;

//players per team constants
const MIN_PLAYER_PER_TEAM_LENGTH = 1;
const MAX_PLAYER_PER_TEAM_LENGTH = 3;
const MIN_PLAYER_PER_TEAM_VALUE = 1;
const MAX_PLAYER_PER_TEAM_VALUE = 100;

//sport image constants
const MAX_IMAGE_SIZE_IN_BYTES = 1048576;

const hasSportNameValidSymbols = sportName => hasStringValidSymbols(sportName, sportNameInvalidSymbols);


const validateSportName = sportName => validateRequiredField(sportName, `${ERROR_BASE_LABEL}sportName`,
                                                                hasSportNameValidSymbols, MIN_SPORTNAME_LENGTH,
                                                                MAX_SPORTNAME_LENGTH);

const validateDisplayName = displayName => validateRequiredField(displayName, `${ERROR_BASE_LABEL}displayName`,
                                                                isStringAlphaNumericOrSpaces, MIN_DISPLAY_NAME_LENGTH,
                                                                MAX_DISPLAY_NAME_LENGTH);

const validatePlayersPerTeam = playersPerTeam => {
    let errorMessage = validateRequiredField(playersPerTeam, `${ERROR_BASE_LABEL}playersPerTeam`,
                                                isStringNumeric, MIN_PLAYER_PER_TEAM_LENGTH,
                                                MAX_PLAYER_PER_TEAM_LENGTH);
    if (!isStringBetweenValues(playersPerTeam, MIN_PLAYER_PER_TEAM_VALUE, MAX_PLAYER_PER_TEAM_VALUE)) {
        errorMessage =`${errorMessage} ${i18next.t(`${ERROR_BASE_LABEL}playersPerTeam.invalidValue`)}`;
    }
    return errorMessage;
}

const validateSportImage = image => {
    let errorLabelBase = `${ERROR_BASE_LABEL}image`;
    let errorMessage = ``;
    if (image && !validImageFormats(image.type)) {
        errorMessage = i18next.t(`${errorLabelBase}.invalidImageFormat`);
    }
    if (image && image.size > MAX_IMAGE_SIZE_IN_BYTES) {
        errorMessage =`${errorMessage} ${i18next.t(`${errorLabelBase}.invalidImageSize`)}`;
    }
    return errorMessage;
}

const validateRequiredSportImage = image => {
    let errorLabelBase = `${ERROR_BASE_LABEL}image`;
    let errorMessage = ``;
    if(!image) {
        errorMessage = i18next.t(`${errorLabelBase}.required`);
    }
    errorMessage = `${errorMessage} ${validateSportImage(image)}`;
    return errorMessage;
}



const CreateSportValidator = {
    validateSportName: validateSportName,
    validateDisplayName: validateDisplayName,
    validatePlayersPerTeam: validatePlayersPerTeam,
    validateSportImage: validateSportImage,
    validateRequiredSportImage: validateRequiredSportImage
};

export default CreateSportValidator;
