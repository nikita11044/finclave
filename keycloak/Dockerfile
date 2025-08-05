FROM quay.io/keycloak/keycloak:26.1.3

# Set admin credentials via environment
ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin
ENV KC_BOOTSTRAP_ADMIN_PASSWORD=admin
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Copy your realm config into the image (optional if you still want to use volume mounts)
COPY realm-config /opt/keycloak/data/import

# Set the default command to start with realm import
CMD ["start-dev", "--import-realm"]

EXPOSE 8080
