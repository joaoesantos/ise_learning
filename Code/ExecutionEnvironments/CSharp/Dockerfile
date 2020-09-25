FROM mcr.microsoft.com/dotnet/core/sdk:3.1
WORKDIR /app

# Copy Folders
COPY NetCoreExecutionEnvironment ./NetCoreExecutionEnvironment
COPY TemplateFolder ./TemplateFolder

RUN dotnet restore ./NetCoreExecutionEnvironment/NetCoreExecutionEnvironment.csproj
CMD dotnet run --project ./NetCoreExecutionEnvironment --configuration Release 

EXPOSE 6002