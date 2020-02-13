import { validateRequiredField } from './utils/RequiredFieldValidators';
import { isStringNumeric } from './utils/StringValidators';

const ERROR_BASE_LABEL = "setMatchScoreForm.errors.";

//score constants
const MIN_SCORE_LENGTH = 1;
const MAX_SCORE_LENGTH = 4;

const validateTeamScore = (score) => {
    return validateRequiredField(score, `${ERROR_BASE_LABEL}teamScore`, isStringNumeric,
                                MIN_SCORE_LENGTH, MAX_SCORE_LENGTH);
}

const SetMatchScoreValidator = {
    validateTeamScore: validateTeamScore
};

export default SetMatchScoreValidator;