import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import moment from 'moment';
import AuthService from '../../../services/AuthService';
import SportService from '../../../services/SportService';
import Loader from '../../Loader';
import CreateMatchValidator from '../validators/CreateMatchValidator';
import CreateMatchForm from './layout';
import MatchService from '../../../services/MatchService';

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
    const errors = {}
    errors.title = CreateMatchValidator.validateTitle(values.title);
    errors.competitivity = CreateMatchValidator.validateCompetitivity(values.competitivity);
    errors.sport = CreateMatchValidator.validateSport(values.sport);
    errors.date = CreateMatchValidator.validateDate(values.date);
    errors.durationHours = CreateMatchValidator.validateDurationHours(values.durationHours);
    errors.durationMinutes = CreateMatchValidator.validateDurationMinutes(values.durationMinutes);
    errors.description = CreateMatchValidator.validateDescription(values.description);
    errors.matchTime = CreateMatchValidator.validateTime(time);
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
            "description": values.description ? values.description : "",
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
        const locationError = CreateMatchValidator.validateLocation(location);
        if(locationError) {
            if (this.mounted) {
                this.setState({ locationError: locationError });
            }
        }
        else {
            if (this.mounted) {
                this.setState({ locationError: null });
            }
            let match = this.loadMatch(values, this.state.image);
            console.log(match);
            const response = await MatchService.createMatch(match);
            console.log(response);
            if (response.status) {
                //TODO handle error
            }
            else {
                const matchKey= response.key;
                this.props.history.push(`matches/${matchKey}`);
            }
        }
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const { locationError } = this.state;
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
                             updateTime={updateTime} 
                             hourOptions={hourOptions}
                             minuteOptions={minuteOptions}
                             sportOptions={sportOptions}
                             updateLocationAndState={this.updateLocationAndState}
                             locationError={locationError}
                             location={location} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

CreateMatchFormContainer = reduxForm({
    form: 'createMatch',
    destroyOnUnmount: true,
    validate
})(CreateMatchFormContainer)

export default CreateMatchFormContainer;