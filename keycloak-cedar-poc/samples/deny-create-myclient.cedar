permit(principal, action, resource);

forbid(
    principal,
    action == Keycloak::Action::"CREATE",
    resource
) when {
    context.resourceType == "CLIENT" &&
    context has clientId &&
    context.clientId == "myclient"
};
