import React from 'react';
import { Link } from 'react-router-dom';
import i18next from 'i18next';

const CreateMatchPoster = () => {
    return (
        <div className="col text-center create-match p-4">
            <p>{i18next.t('match.cantFindMatch')}</p>
            <Link id="create-match-btn" className="btn btn-white-succ" to="/createMatch" role="button">
                {i18next.t('createMatch')}
            </Link>
        </div>
    )
}

export default CreateMatchPoster;