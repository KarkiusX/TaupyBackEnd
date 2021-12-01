import { instance } from "./Global";


const Reducer = (state, action) => {
    switch (action.type) {
        case 'Items':
            var products = action.data;
            products.sort((a, b) => a.price - b.price);
            return{
                ...state,
                products : products
            };
        case 'Markets':
            return{
                ...state,
                markets : action.data
            }
        case 'Active':
            return {
              ...state,
              loggedIn : true,
              userinfo : action.data
            };
        case 'Logout':
            return{
                ...state,
                loggedIn : false,
                userInfo : undefined,
            }
    }
};

export default Reducer;