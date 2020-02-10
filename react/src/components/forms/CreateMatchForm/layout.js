import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import FormTitle from '../elements/FormTitle';
import RenderInput from '../inputs/RenderInput';
import SubmitButton from '../elements/SubmitButton';
import FormComment from '../elements/FormComment';
import RenderSelect from '../inputs/SelectInput';
import CompetitiveRadio from '../../match/CompetitiveRadio';
import RenderTextArea from '../inputs/RenderTextArea';
import MatchLocation from '../inputs/MatchLocation';
import SubLocationInput from '../inputs/SubLocationInput';
import RenderMatchDatePicker from '../inputs/RenderMatchDatePicker';
import MatchTime from './components/MatchTime';
import MatchDuration from './components/MatchDuration';

const CreateMatchForm = ({ handleSubmit, submitting, onSubmit, 
                            sportOptions, updateTime, hourOptions,
                            minuteOptions, updateLocationAndState,
                            locationError, location, currentTime,
                            changeFieldsValue, touchField }) => {
    return (
        <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid create-match-container offset-sm-2
                                    col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6
                                    offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <Field name="title" inputType="text" required={true}
                                    label={i18next.t('createMatchForm.matchName')} 
                                     component={RenderInput} />
                            <CompetitiveRadio />
                            <Field name="sport" label={i18next.t('createMatchForm.sport')} 
                                    required={true} options={sportOptions}
                                    defaultText={i18next.t('createMatchForm.chooseSport')}
                                    component={RenderSelect} />
                            <Field name="date"  id="matchDate" required={true} 
                                    label={i18next.t('createMatchForm.date')}
                                    smallText={i18next.t('createUserForm.dateFormat')}
                                    component={RenderMatchDatePicker} />
                            <div className="form-row">
                                <MatchTime changeFieldsValue={changeFieldsValue} 
                                                touchField={touchField} />
                                <MatchDuration hourOptions={hourOptions} 
                                                minuteOptions={minuteOptions} />
                            </div>
                            <Field name="description" inputType="text-area"
                                    label={i18next.t('createMatchForm.description')} 
                                    required={false} component={RenderTextArea} />
                            <Field name="matchLocation" errorMessage={locationError}
                                    updateLocationAndState={updateLocationAndState}
                                    location={location} component={MatchLocation} />
                            <SubLocationInput label={i18next.t('location.country')}
                                                id="country" divStyle="form-group"
                                                value={location.country ? location.country : ""} />
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.street')} id="route"
                                                value={location.street ? location.street : ""} 
                                                divStyle="form-group col-9" />
                                <SubLocationInput label={i18next.t('location.number')} id="number"
                                                value={location.number ? location.number : ""} 
                                                divStyle="form-group col-3" />
                            </div>
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.city')} id="locality"
                                                    value={location.city ? location.city : ""} 
                                                    divStyle="form-group col-6" />
                                <SubLocationInput label={i18next.t('location.state')} 
                                                    id="administrative_area_level_1"
                                                    value={location.state ? location.state : ""} 
                                                    divStyle="form-group col-6" />
                            </div>
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" 
                                            text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createMatchForm.createMatch')} 
                                            divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                            submitting={submitting} />
                        </form>
                    </div>
                </div>
            </div>
    );
}

CreateMatchForm.propTypes = {
        handleSubmit: PropTypes.func.isRequired,
        onSubmit: PropTypes.func.isRequired,
        sportOptions: PropTypes.array.isRequired,
        hourOptions: PropTypes.array.isRequired,
        minuteOptions: PropTypes.array.isRequired,
        updateLocationAndState: PropTypes.func.isRequired,
        locationError: PropTypes.string,
        location: PropTypes.object.isRequired,
        changeFieldsValue: PropTypes.func.isRequired,
        touchField: PropTypes.func.isRequired
}

export default CreateMatchForm;