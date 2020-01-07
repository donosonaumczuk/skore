import React from 'react';
import { create } from 'axios';

const api = create({ baseURL: '/api/' });

export default api;