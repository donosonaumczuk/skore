import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import moment from 'moment';
import i18next from 'i18next';
import FormTitle from './inputs/FormTitle';
import RenderInput from './inputs/RenderInput';
import SubmitButton from './inputs/SubmitButton';
import AuthService from '../../services/AuthService';
import FormComment from './inputs/FormComment';
import RenderSelect from './inputs/SelectInput';
import SportService from '../../services/SportService';
import Loader from '../Loader';
import CompetitiveRadio from '../match/CompetitiveRadio';
import RenderTextArea from './inputs/RenderTextArea';
import MatchLocation from './inputs/MatchLocation';
import SubLocationInput from './inputs/SubLocationInput';
import RenderMatchDatePicker from './inputs/RenderMatchDatePicker';
import RenderTimePicker from './inputs/RenderTimePicker';
import CreateMatchValidator from './validators/CreateMatchValidator';

var location = {
    "country": null,
    "state": null,
    "city": null,
    "street": null,    
    "number": null
};

var time = {
    "hour": null,
    "minutes": null
};

//TODO only show them after submit is pressed
const customErrors = {
    locationError: null,
    timeError: null,
    active: false
}

const updateLocation = home => {
    location.street = home.street;
    location.city = home.city;
    location.state = home.state;
    location.country = home.country;
    location.number = home.number;
}

const updateTime = newTime => {
    const timeArray = moment(newTime).format("hh:mm").split(":");
    time.hour = parseInt(timeArray[0]);
    time.minutes = parseInt(timeArray[1]);  
}

const validate = values => {
    console.log("entro");
    const errors = {}
    errors.title = CreateMatchValidator.validateTitle(values.title);
    errors.competitivity = CreateMatchValidator.validateCompetitivity(values.competitivity);
    errors.sport = CreateMatchValidator.validateSport(values.sport);
    errors.date = CreateMatchValidator.validateDate(values.date);
    errors.durationHours = CreateMatchValidator.validateDurationHours(values.durationHours);
    errors.durationMinutes = CreateMatchValidator.validateDurationMinutes(values.durationMinutes);
    errors.description = CreateMatchValidator.validateDescription(values.description);
    customErrors.timeError = CreateMatchValidator.validateTime(time);
    customErrors.locationError = CreateMatchValidator.validateLocation(location);
    return errors;
}


class CreateMatchForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            sports: null,
        };
    }
    
    componentDidMount = async () => {
        this.mounted = true;
        let response = await SportService.getSports();
        if (response.status) {
            //TODO handle error
        }
        else if (this.mounted) {
            //TODO find a way to request all sports
            this.setState({
                sports: response.sports
            });
        }
    }

    generateSportOptions = () => {
        return (
            this.state.sports.map( sport => 
                <option key={sport.sportName} value={sport.sportName}>
                    {sport.displayName}
                </option>
            )
        );
    }

    generateHourOptions = () => {
        let hours = [0, 1, 2, 3, 4, 5];
        return (
            hours.map( hour => 
                <option key={hour} value={hour}>
                    {hour}
                </option>
            )
        );
    }

    generateMinuteOptions = () => {
        let minutes = [];
        for (let i = 0; i <= 55; i += 5) {
            minutes.push(i);
        }
        return (
            minutes.map( minute => 
                <option key={minute} value={minute}>
                    {minute}
                </option>
            )
        );
    }

    getDate = (date) => {
        let newDate;
        if (date) {
            const dateArray = moment(date).format("MM/DD/YYYY").split("/");
            //TODO get Time and calculate its value
            newDate = {
                year: parseInt(dateArray[2]),
                month: parseInt(dateArray[0]),
                day: parseInt(dateArray[1]),
            };
        }
        return newDate;
    }

    getDurationMinutes = (hours, minutes) => {
        const numericHours = parseInt(hours);
        const numericMinutes = parseInt(minutes);
        return numericHours * 60 + numericMinutes;
    }

    loadMatch = (values) => {
        const date = this.getDate(values.date);
        const durationMinutes = this.getDurationMinutes(values.durationHours, values.durationMinutes);
        const match = {
            "title": values.title,
            "description": values.description,
            "sport": values.sport,
            "date" : {
                "year": date.year,
                "monthNumber": date.month,
                "dayOfMonth": date.day,
            },
            "time": {
                "hour": time.hour,
                "minute": time.minutes
            },
            "minutesOfDuration": durationMinutes,
            "location": {
                "country": location.country ? location.country : null,
                "state": location.state ? location.state : null,
                "city": location.city ? location.city : null,
                "street": "" + location.street + " " + location.number
            },
            "individual": true,
            "competitive": values.competitivity === "competitive"
        };
        return match;
    }

    updateLocationAndState = home => {
        updateLocation(home);
        if (this.mounted) {
            this.setState({
                modifyingLocation: true
            });
        }
    }

    onSubmit = async (values) => {
        let match = this.loadMatch(values, this.state.image);
        console.log(match);//TODO is here just to prevent warning
        //TODO implement post to endpoint and then redirect, when ednpoint is created
        
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        const hourOptions = this.generateHourOptions();
        const minuteOptions = this.generateMinuteOptions();

        if (!currentUser) {
            return <Redirect to="/" />
        }
        else if (!this.state.sports) {
            return <Loader />
        }
        const sportOptions = this.generateSportOptions();

        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid create-match-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            <Field name="title" label={i18next.t('createMatchForm.matchName')} 
                                    inputType="text" required={true} component={RenderInput} />
                            <CompetitiveRadio error={this.state.competitivityRequired ? true : false }/>
                            <Field name="sport" label={i18next.t('createMatchForm.sport')} 
                                    required={true} defaultText={i18next.t('createMatchForm.chooseSport')}
                                    options={sportOptions} component={RenderSelect} />
                            <Field name="date" label={i18next.t('createMatchForm.date')} id="matchDate"
                                    required={true} smallText={i18next.t('createUserForm.dateFormat')}
                                    component={RenderMatchDatePicker} />
                            <div className="form-row">
                                <div className="form-group col-6">
                                    <div className="form-row">
                                        <div className="col-12">
                                            <Field name="matchTime" label={i18next.t('createMatchForm.from')} 
                                                    updateTime={updateTime} errorMessage={customErrors.timeError}
                                                    component={RenderTimePicker} />
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group col-6">
                                    <label>{i18next.t('createMatchForm.duration')}*</label>
                                        <div className="form-row">
                                            <div className="col-6">
                                                <Field name="durationHours" required={false} 
                                                        defaultText={i18next.t('createMatchForm.chooseHour')}
                                                        options={hourOptions} component={RenderSelect} />  
                                            </div>
                                            <div className="col-6">
                                                <Field name="durationMinutes" required={false} options={minuteOptions}
                                                        defaultText={i18next.t('createMatchForm.chooseMinute')}
                                                        component={RenderSelect} />  
                                            </div>
                                        </div>
                                </div>
                            </div>
                            <Field name="description" label={i18next.t('createMatchForm.description')} 
                                    inputType="text-area" required={false} component={RenderTextArea} />
                            <Field name="matchLocation" updateLocationAndState={this.updateLocationAndState}
                                    errorMessage={customErrors.locationError} component={MatchLocation} />
                            <SubLocationInput label={i18next.t('location.country')} id="country"
                                                value={location.country ? location.country : ""} 
                                                divStyle="form-group" />
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
                                <SubLocationInput label={i18next.t('location.state')} id="administrative_area_level_1"
                                                    value={location.state ? location.state : ""} 
                                                    divStyle="form-group col-6" />
                            </div>
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" 
                                            text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createMatchForm.createMatch')} divStyle="text-center" 
                                            buttonStyle="btn btn-green mb-2" submitting={submitting} />
                        </form>
                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

CreateMatchForm = reduxForm({
    form: 'createMatch',
    destroyOnUnmount: false,
    validate,
})(CreateMatchForm)

export default CreateMatchForm;