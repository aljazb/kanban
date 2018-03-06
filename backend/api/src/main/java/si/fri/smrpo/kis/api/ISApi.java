package si.fri.smrpo.kis.api;

import si.fri.smrpo.kis.api.client.authorization.provider.base.ISApiAuthProvider;

public class ISApi {

    private ISApiCore core;

    /*public ISCrudResource<Address> address;
    public ISLoginCrudResource<UserAccount> customer;
    public OrderCrudResource order;
    public ISCrudResource<Product> product;
    public ISLoginCrudResource<Administrator> administrator;*/

    public ISApi(ISApiConfiguration configuration) {
        this(configuration, null);
    }

    public ISApi(ISApiConfiguration configuration, ISApiAuthProvider provider) {
        core = new ISApiCore(configuration, provider);

        initResources();
    }

    private void initResources(){
        /*address = new ISCrudResource<>(core, Address.class);
        customer = new ISLoginCrudResource<>(core, UserAccount.class);
        order = new OrderCrudResource(core);
        product = new ISCrudResource<>(core, Product.class);
        administrator = new ISLoginCrudResource<>(core, Administrator.class);*/
    }

    public ISApiCore getCore() {
        return core;
    }
}
