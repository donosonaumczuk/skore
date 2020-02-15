import i18next from 'i18next';
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

const validateRepeatTeamOneScore = (repeatedScore, teamOneScore) => {
    let errorMessage = validateRequiredField(repeatedScore, `${ERROR_BASE_LABEL}teamScore`,
                                                isStringNumeric, MIN_SCORE_LENGTH,
                                                MAX_SCORE_LENGTH);
    if (repeatedScore !== teamOneScore) {
        errorMessage = `${errorMessage} ${i18next.t(`${ERROR_BASE_LABEL}repeatedScore.teamOneDoesNotMatch`)}`;
    }
    return errorMessage;
}

const validateRepeatTeamTwoScore = (repeatedScore, teamTwoScore) => {
    let errorMessage = validateRequiredField(repeatedScore, `${ERROR_BASE_LABEL}teamScore`,
                                                isStringNumeric, MIN_SCORE_LENGTH,
                                                MAX_SCORE_LENGTH);
    if (repeatedScore !== teamTwoScore) {
        errorMessage = `${errorMessage} ${i18next.t(`${ERROR_BASE_LABEL}repeatedScore.teamTwoDoesNotMatch`)}`;
    }
    return errorMessage;
}

const SetMatchScoreValidator = {
    validateTeamScore: validateTeamScore,
    validateRepeatTeamOneScore: validateRepeatTeamOneScore,
    validateRepeatTeamTwoScore: validateRepeatTeamTwoScore
};

export default SetMatchScoreValidator;