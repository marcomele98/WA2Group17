const URL = '/API/';

async function getProducts() {
    return new Promise((resolve, reject) => {
        fetch(URL + 'products/')
            .then((response) => {
                if (response.ok) {
                    resolve(response.json());
                } else {
                    response.json()
                        .then((obj) => { reject(obj); })
                        .catch(() => { reject({ error: "Cannot parse server response." }) });
                }
            }).catch(() => { reject({ error: "Cannot communicate with the server." }) });
    });
};

async function getProductByEan(ean) {
    return new Promise((resolve, reject) => {
        fetch(URL + 'products/' + ean)
            .then((response) => {
                if (response.ok) {
                    resolve(response.json());
                } else {
                    response.json()
                        .then((obj) => { reject(obj); })
                        .catch(() => { reject({ error: "Cannot parse server response." }) });
                }
            }).catch(() => { reject({ error: "Cannot communicate with the server." }) });
    });
}

async function getProfileByEmail(email) {
    return new Promise((resolve, reject) => {
        fetch(URL + 'profiles/' + email)
            .then((response) => {
                if (response.ok) {
                    resolve(response.json());
                } else {
                    response.json()
                        .then((obj) => { reject(obj); })
                        .catch(() => { reject({ error: "Cannot parse server response." }) });
                }
            }).catch(() => { reject({ error: "Cannot communicate with the server." }) });
    });
}


async function createProfile(profile) {
    return new Promise((resolve, reject) => {
      fetch(URL + 'profiles', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(profile),
      }).then((response) => {
        if (response.ok) {
          resolve(response.json());
        } else {
          response.json()
            .then((obj) => { reject(obj); })
            .catch(() => { reject({ error: "Cannot parse server response." }) });
        }
      }).catch(() => { reject({ error: "Cannot communicate with the server." }) }); // connection errors
    });
  }


  async function updateProfile(profile, email) {
    return new Promise((resolve, reject) => {
      fetch(URL + 'profiles/'+ email, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(profile),
      }).then((response) => {
        if (response.ok) {
          resolve(null);
        } else {
          response.json()
            .then((obj) => { reject(obj); }) // error message in the response body
            .catch(() => { reject({ error: "Cannot parse server response." }) }); // something else
        }
      }).catch(() => { reject({ error: "Cannot communicate with the server." }) }); // connection errors
    });
  }



const API = { getProducts, getProductByEan, getProfileByEmail, createProfile, updateProfile }

export default API;