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
import LocationInput from './inputs/LocationInput';
import SubLocationInput from './inputs/SubLocationInput';
import RenderMatchDatePicker from './inputs/RenderMatchDatePicker';
import RenderTimePicker from './inputs/RenderTimePicker';

class CreateMatchForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            image: null,
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

    updateLocation = home => {
        if (this.mounted) {
            this.setState({
                street: home.street,
                city: home.city,
                state: home.state,
                country: home.country,
                number: home.number          
            });
        }
    }

    getDateTime = (date, time) => {
        const dateArray = moment(date).format("MM/DD/YYYY").split("/");
        //TODO get Time and calculate its value
        const dateTime = {
            year: parseInt(dateArray[2]),
            month: parseInt(dateArray[0]),
            day: parseInt(dateArray[1]),
            "hour": 0,
            "minutes": 0,
        };
        return dateTime;
    }

    getDurationMinutes = (hours, minutes) => {
        const numericHours = parseInt(hours);
        const numericMinutes = parseInt(minutes);
        return numericHours * 60 + numericMinutes;
    }

    loadMatch = (values, image) => {
        const dateTime = this.getDateTime(values.date, values.matchTime);
        const durationMinutes = this.getDurationMinutes(values.durationHours, values.durationMinutes);
        const match = {
            "title": values.title,
            "description": values.description,
            "sport": values.sport,
            "date" : {
                "year": dateTime.year,
                "monthNumber": dateTime.month,
                "dayOfMonth": dateTime.day,
            },
            "time": {
                "hour": dateTime.hour,
                "minute": dateTime.minutes
            },
            "minutesOfDuration": durationMinutes,
            "location": {
                "country": this.state.country ? this.state.country : null,
                "state": this.state.state ? this.state.state : null,
                "city": this.state.city ? this.state.city : null,
                "street": "" + this.state.street + " " + this.state.number
            },
            "individual": true,
            "competitive": values.competitivity === "competitive"
        };
        return match;
    }

    onSubmit = async (values) => {
        let match = this.loadMatch(values, this.state.image);
        console.log("matchTime");
        console.log(values.matchTime);
        console.log("entro");
        console.log(match);
        // const res = await UserService.createUser(user);
        // if (res.status) {
        //    //TODO handle error
        // }
        // else {
        //     this.props.history.push(`/confirmAccount`);
        // }
       //TODO implement
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
                            <CompetitiveRadio />
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
                                            <Field name="matchTime" label="From" component={RenderTimePicker} />
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
                            <LocationInput updateLocation={this.updateLocation} />
                            <SubLocationInput label={i18next.t('location.country')} id="country" path="country"
                                                value={this.state.country ? this.state.country : ""} 
                                                divStyle="form-group" />
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.street')} id="route"
                                                value={this.state.street ? this.state.street : ""} 
                                                divStyle="form-group col-9" />
                                <SubLocationInput label={i18next.t('location.number')} id="number"
                                                value={this.state.number ? this.state.number : ""} 
                                                divStyle="form-group col-3" />
                            </div>
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.city')} id="locality" path="city" 
                                                    value={this.state.city ? this.state.city : ""} 
                                                    divStyle="form-group col-6" />
                                <SubLocationInput label={i18next.t('location.state')} id="administrative_area_level_1"
                                                    path="state" value={this.state.state ? this.state.state : ""} 
                                                    divStyle="form-group col-6" />
                            </div>
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createMatchForm.createMatch')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
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
    destroyOnUnmount: false, // set to true to remove data on refresh
})(CreateMatchForm)

export default CreateMatchForm;