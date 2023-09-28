import axios from "axios";

axios.defaults.withCredentials = true;
const APIURL = new URL("http://localhost:8081/API/");

axios.interceptors.request.use(function (config) {
    // Do something before request is sent
    document.body.classList.add("loading-overlay");
    return config;
}, undefined);

axios.interceptors.response.use(
    function (response) {
        // Do something with response data
        document.body.classList.remove("loading-overlay");
        return response;
    },
    async function (error) {

        if (!error) {
            return Promise.reject(error);
        }
        const originalRequest = error?.config;
        let refreshTokenError, res;
        if (
            error?.response?.status === 401 &&
            !originalRequest._retry &&
            originalRequest?.url?.pathname !== "/API/refresh" &&
            originalRequest?.url?.pathname !== "/API/login"
        ) {
            originalRequest._retry = true;
            let url = new URL("refresh", APIURL);
            const params = new URLSearchParams();
            params.append("refreshToken", localStorage.getItem("refreshToken"));
            url.search = params.toString();
            [refreshTokenError, res] = await axios
                .post(url, undefined, undefined)
                .then(async (response) => {
                    //handle success
                    localStorage.setItem("accessToken", response.data.accessToken);
                    localStorage.setItem("refreshToken", response.data.refreshToken);
                    error.config.headers[
                        "Authorization"
                        ] = `Bearer ${response.data.accessToken}`;
                    return [null, await axios.request(originalRequest)];
                })
                .catch(function (error) {
                    if (error.response) {
                        // Request made and server responded
                        console.log(error.response.data);
                    } else if (error.request) {
                        // The request was made but no response was received
                        console.log(error.request);
                    } else {
                        // Something happened in setting up the request that triggered an Error, 404 not found
                        console.log("Error", error.message);
                    }
                });
            if (refreshTokenError) {
                document.body.classList.remove("loading-overlay");
                return Promise.reject(refreshTokenError);
            }
            return Promise.resolve(res);
        } else if (originalRequest?.url?.pathname === "/API/refresh") {
            if(localStorage.getItem("refreshToken") !== null && localStorage.getItem("accessToken") !== null){
                localStorage.removeItem("accessToken");
                localStorage.removeItem("refreshToken");
                window.location.reload();
            }
        }
        document.body.classList.remove("loading-overlay");
        return Promise.reject(error.response);
    }
);


const refreshToken = async () => {
    let url = new URL("refresh", APIURL);
    const params = new URLSearchParams();
    params.append("refreshToken", localStorage.getItem("refreshToken"));
    url.search = params.toString();
    let res = await axios.post(url, undefined, undefined);
    localStorage.setItem("accessToken", res.data.accessToken);
    localStorage.setItem("refreshToken", res.data.refreshToken);
    return;
};

const getAuthHeader = () => {
    return {Authorization: `Bearer ${localStorage.getItem("accessToken")}`};
};

const axiosInstance = axios;

async function logIn(username, password) {
    // call: POST /api/sessions
    try {
        let response = await axios.post(new URL("login", APIURL), {
            email: username,
            password: password,
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getProductByEan(ean) {
    // call: GET /api/products/:ean
    try {
        let response = await axios.get(new URL("products/" + ean, APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getCustomerByEmail(email) {
    // call: GET /api/profiles/:email
    try {
        let response = await axios.get(
            new URL("cashier/profiles/" + email, APIURL),
            {
                headers: getAuthHeader(),
            }
        );
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function createWarranty(warranty) {
    // call: POST /api/warranties
    try {
        let response = await axios.post(
            new URL("cashier/warranties", APIURL),
            warranty,
            {
                headers: getAuthHeader(),
            }
        );
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function createProfile(profile) {
    // call: POST /api/profiles
    try {
        let response = await axios.post(new URL("signup", APIURL), profile);
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getWorkersProfiles() {
    // call: GET /api/manager/profiles
    try {
        let response = await axios.get(new URL("manager/profiles", APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getWorkerByEmail(email) {
    // call: GET /api/profiles/:email
    try {
        let response = await axios.get(
            new URL("manager/profiles/" + email, APIURL),
            {
                headers: getAuthHeader(),
            }
        );
        return response.data;
    } catch (err) {
        throw err;
    }
}


async function createWorker(user) {
    try {
        let response = await axios.post(new URL("manager/profiles", APIURL), user, {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function updateWorker(email, user) {
    try {
        let response = await axios.put(new URL("manager/profiles/" + email, APIURL), user, {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

async function deleteWorker(email) {
    try {
        let response = await axios.delete(new URL("manager/profiles/" + email, APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function signup(user) {
    try {
        let response = await axios.post(new URL("signup", APIURL), user);
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getWarranties() {
    // call: GET /api/warranties
    try {
        let response = await axios.get(new URL("customer/warranties", APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getWarranty(id) {
    // call: GET /api/warranties/:id
    try {
        let response = await axios.get(new URL("customer/warranties/" + id, APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function createTicket(ticket) {
    // call: POST /api/tickets
    try {
        let response = await axios.post(new URL("customer/tickets", APIURL), ticket, {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function addMessage(message, id) {
    // call: PUT /api/tickets/messages/:id
    try {
        let response = await axios.put(new URL("tickets/message/" +id, APIURL), message, {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function uploadAttachment(attachment) {
    try {
        const formData = new FormData();
        formData.append('file', attachment);
        const response = await axios.post(new URL("attachments/upload", APIURL), formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                ...getAuthHeader()
            },
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function downloadAttachment(attachmentId) {
    try {
        const response = await axios.get(new URL('attachments/download/'+attachmentId, APIURL), {
            headers: {
                ...getAuthHeader()
            },
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getTickets() {
    // call: GET /api/tickets
    try {
        let response = await axios.get(new URL("customer/tickets", APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getTicket(id) {
    // call: GET /api/tickets/:id
    try {
        let response = await axios.get(new URL("tickets/" +id, APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getAssignedTickets() {
    // call: GET /api/tickets
    try {
        let response = await axios.get(new URL("manager/tickets/assigned", APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function getOpenTickets() {
    // call: GET /api/manager/tickets/open
    try {
        let response = await axios.get(new URL("manager/tickets/open", APIURL), {
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function assignTicket(assignTicketDTO, id) {
    // call: POST /api/manager/tickets/assign
    try {
        let response = await axios.put(new URL("manager/tickets/assign/"+ id, APIURL), assignTicketDTO,{
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function closeTicket(ticketId) {
    try {
        let response = await axios.put(new URL("tickets/close/"+ ticketId, APIURL), undefined,{
            headers: getAuthHeader(),
        });
        return response.data;
    }
    catch (err) {
        throw err;
    }
}

async function resolveTicket(ticketId){
    try {
        let response = await axios.put(new URL("expert/tickets/resolve/"+ ticketId, APIURL), undefined,{
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}

async function reopenTicket(ticketId){
    try {
        let response = await axios.put(new URL("customer/tickets/reopen/"+ ticketId, APIURL), undefined,{
            headers: getAuthHeader(),
        });
        return response.data;
    } catch (err) {
        throw err;
    }
}


const API = {
    getWorkersProfiles,
    getProductByEan,
    getCustomerByEmail,
    createProfile,
    createWarranty,
    logIn,
    getWorkerByEmail,
    createWorker,
    updateWorker,
    deleteWorker,
    signup,
    getWarranties,
    getWarranty,
    createTicket,
    uploadAttachment,
    getTickets,
    getOpenTickets,
    assignTicket,
    getAssignedTickets,
    getTicket,
    addMessage,
    refreshToken,
    downloadAttachment,
    closeTicket,
    resolveTicket,
    reopenTicket
};

export default API;
