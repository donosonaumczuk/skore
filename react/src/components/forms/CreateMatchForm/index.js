import React, { Component } from 'react';
import { connect } from 'react-redux'
import { reduxForm, change, touch, formValueSelector } from 'redux-form';
import { Redirect } from 'react-router-dom';
import moment from 'moment';
import AuthService from '../../../services/AuthService';
import SportService from '../../../services/SportService';
import Loader from '../../Loader';
import CreateMatchValidator from '../validators/CreateMatchValidator';
import CreateMatchForm from './layout';
import MatchService from '../../../services/MatchService';


const validate = values => {
    const errors = {}
    errors.title = CreateMatchValidator.validateTitle(values.title);
    errors.competitivity = CreateMatchValidator.validateCompetitivity(values.competitivity);
    errors.sport = CreateMatchValidator.validateSport(values.sport);
    errors.date = CreateMatchValidator.validateDate(values.date);
    errors.durationHours = CreateMatchValidator.validateDurationHours(values.durationHours);
    errors.durationMinutes = CreateMatchValidator.validateDurationMinutes(values.durationMinutes);
    errors.description = CreateMatchValidator.validateDescription(values.description);
    errors.matchTime = CreateMatchValidator.validateTime(values.matchTime);
    errors.matchLocation = CreateMatchValidator.validateLocation(values.matchLocation);
    return errors;
}

class CreateMatchFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            sports: null,
        };
    }

    getAllSports = async () => {
        let hasMore = true;
        while (hasMore) {
            let response = await SportService.getSports();
            if (response.status) {
                hasMore = false
                //TODO handle error only 500 or 400
            }
            else if (this.mounted) {
                hasMore = false;
            //TODO find a way to request all sports
                this.setState({
                    sports: response.sports
                });
            }
        }
    }
    
    componentDidMount = async () => {
        this.mounted = true;
        getAllSports();
        
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
        const { country, state, city, street, number } = values.matchLocation;
        const durationMinutes = this.getDurationMinutes(values.durationHours, values.durationMinutes);
        const match = {
            "title": values.title,
            "description": values.description ? values.description : "",
            "sport": values.sport,
            "date" : {
                "year": date.year,
                "monthNumber": date.month,
                "dayOfMonth": date.day,
            },
            "time": {
                "hour": values.matchTime.hour,
                "minute": values.matchTime.minutes
            },
            "minutesOfDuration": durationMinutes,
            "location": {
                "country": country ? country : null,
                "state": state ? state : null,
                "city": city ? city : null,
                "street": "" + street + " " + number
            },
            "individual": true,
            "competitive": values.competitivity === "competitive"
        };
        return match;
    }


    onSubmit = async (values) => {
        let match = this.loadMatch(values, this.state.image);
        const response = await MatchService.createMatch(match);
        if (response.status) {
            //TODO handle error only 409 
        }
        else {
            const matchKey= response.key;
            this.props.history.push(`match/${matchKey}`);
        }
        
    }

    render() {
        const { handleSubmit, submitting, change, touch, matchLocation } = this.props; 
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
            <CreateMatchForm handleSubmit={handleSubmit}
                             submitting={submitting}
                             onSubmit={this.onSubmit}
                             hourOptions={hourOptions}
                             minuteOptions={minuteOptions}
                             sportOptions={sportOptions}
                             location={matchLocation} changeFieldsValue={change}
                             touchField={touch} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

CreateMatchFormContainer = reduxForm({
    form: 'createMatch',
    destroyOnUnmount: true,
    validate,
    change,
    touch
})(CreateMatchFormContainer)

const selector = formValueSelector('createMatch');

CreateMatchFormContainer = connect(state => {    
    let matchLocation = selector(state, 'matchLocation')
    if (!matchLocation) {
        matchLocation = {
            "country": null,
            "state": null,
            "city": null,
            "street": null,    
            "number": null
        }
    }
    return {
      matchLocation: matchLocation
    }
})(CreateMatchFormContainer)  

export default CreateMatchFormContainer;