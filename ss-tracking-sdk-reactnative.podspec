require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "ss-tracking-sdk-reactnative"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://git.vietbando.net/sovereignsolution/ss-tracking-sdk-reactnative.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm,swift}"

  s.dependency "React-Core"
  s.dependency "TCTracking"
#  s.dependency 'AlamofireObjectMapper'
#  s.dependency 'SwiftyJSON'
#  s.dependency 'Alamofire', '4.8.0'
#  s.dependency 'ObjectMapper'
end
