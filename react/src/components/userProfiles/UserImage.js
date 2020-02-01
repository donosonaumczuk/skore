import React from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';

//TODO maybe add loader until loads picture?
const UserImage = ({ styleClass, imageUrl }) => {
    return (
        <div className="row text-center">
            <div className="col">
                <img className={styleClass} src={imageUrl? imageUrl : ""} alt={i18next.t('profile.imageDescription')} />
            </div>
        </div>
    )
}

UserImage.propTypes = {
    imageUrl: Proptypes.string,
    styleClass: Proptypes.string.isRequired
}

export default UserImage;

//TODO if we need to request data instead of url
// import React, { Component } from 'react';
// import UserService from './../../services/UserService';
// import Proptypes from 'prop-types';

// class UserImage extends Component {
//     constructor(props) {
//         super(props);
//         this.state = {
//             binaryImage: null
//         };
//     }

//     async componentDidMount() {  
//         let image = await UserService.getUserImage(this.props.username);
//         this.setState({ binaryImage: image });
//     }

//     render() {
//         const { styleClass } = this.props;
//         return (
//             <div className="row text-center">
//                 <div className="col">
//                     <img className={styleClass} src="http://localhost:8080/api/users/donosonaumczuk/image" />
//                 </div>
//             </div>
//         );
//     }
// }

// UserImage.propTypes = {
//     username: Proptypes.string,
//     styleClass: Proptypes.string.isRequired
// }

// export default UserImage;
