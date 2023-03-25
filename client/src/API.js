const URL = '/api/';

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