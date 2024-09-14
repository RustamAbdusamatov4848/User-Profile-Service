import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    scenarios: {
        create_and_update_user: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '15s', target: 5},
                {duration: '30s', target: 5},
                {duration: '15s', target: 0},
            ],
            exec: 'createAndUpdateUser',
        },
        fetch_user_and_contacts: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '15s', target: 5},
                {duration: '30s', target: 5},
                {duration: '15s', target: 0},
            ],
            exec: 'fetchUserAndContacts',
        },
    },
    thresholds: {
        'http_req_duration{scenario:create_and_update_user}': ['p(95)<500'],
        'http_req_duration{scenario:fetch_user_and_contacts}': ['p(95)<500'],
        'http_req_failed{scenario:create_and_update_user}': ['rate<0.01'],
        'http_req_failed{scenario:fetch_user_and_contacts}': ['rate<0.01'],
        'checks{scenario:create_and_update_user}': ['rate>0.95'],
        'checks{scenario:fetch_user_and_contacts}': ['rate>0.95'],
    },
};

const SERVICE_URL = 'http://app:8080/v1/users';

export function createAndUpdateUser() {

    const newUserDto = {
        firstName: generateUniqueEmail("firstname"),
        lastName: generateUniqueEmail("lastname"),
        email: generateUniqueEmail("email") + "@example.com"
    };

    const createUser = http.post(`${SERVICE_URL}`, JSON.stringify(newUserDto), {
        headers: {'Content-Type': 'application/json'},
        tags: {name: 'CreateUser'}
    });

    check(createUser, {
        'is status 201': (r) => r.status === 201,
    }, {name: 'CreateUser'});

    const createdUserId = JSON.parse(createUser.body).id;

    const updatedUserDto = {...newUserDto, id: createdUserId, lastName: generateUniqueEmail("lastname")};

    const updateUser = http.put(`${SERVICE_URL}`, JSON.stringify(updatedUserDto), {
        headers: {'Content-Type': 'application/json'},
        tags: {name: 'UpdateUser'}
    });

    check(updateUser, {
        'is status 204': (r) => r.status === 204,
    }, {name: 'UpdateUser'});
}

export function fetchUserAndContacts() {
    const userId = 1;

    const getUser = http.get(`${SERVICE_URL}/${userId}`, {
        tags: {name: 'FetchUser'}
    });

    check(getUser, {
        'is status 200': (r) => r.status === 200,
    }, {name: 'FetchUser'});

    const getUserContact = http.get(`${SERVICE_URL}/${userId}/contacts`, {
        tags: {name: 'FetchUserContact'}
    });

    check(getUserContact, {
        'is status 200': (r) => r.status === 200,
    }, {name: 'FetchUserContact'});
}

function generateUniqueEmail(string) {
    const randomDigit = getRandomInt(1,10000);
    const timestamp = new Date().getTime();
    return string + `${timestamp}` + randomDigit;
}

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
