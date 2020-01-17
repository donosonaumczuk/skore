
import { isString, isNumber, isBoolean } from './util/TypeValidators';

const requiredMatchPropType = (props, propName, componentName) => {
    const value = props[propName];
    //TODO add validations of value.date and value.time
    if (value && isString(value.location) && isBoolean(value.competitive) && isString(value.sportName) &&
        isString(value.creator) && isString(value.title) && isNumber(value.durationInMinutes)) {
        return null;
    }
    else {
        return new TypeError(`Invalid Match Prop Value: ${value} for ${propName} in ${componentName}`);
    }
}

const HomeMatchPropType = (props, propName, componentName) => {
    if (props[propName] == null) {
        return null;
    }
    return requiredMatchPropType(props, propName, componentName);
};

HomeMatchPropType.isRequired = requiredMatchPropType;

export default HomeMatchPropType;
