FROM mcr.microsoft.com/dotnet/core/sdk:3.1
WORKDIR /app
# Copy Folders
COPY NetCoreExecutionEnvironment ./NetCoreExecutionEnvironment
COPY TemplateFolder ./TemplateFolder
COPY startup.sh .
RUN chmod +x startup.sh
EXPOSE 6002
ENTRYPOINT [ "./startup.sh" ]