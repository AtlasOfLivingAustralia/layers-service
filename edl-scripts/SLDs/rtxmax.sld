<?xml version="1.0" encoding="UTF-8"?><sld:UserStyle xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
  <sld:Name>raster</sld:Name>
  <sld:Title>A very simple color map</sld:Title>
  <sld:Abstract>A very basic color map</sld:Abstract>
  <sld:FeatureTypeStyle>
    <sld:Name>name</sld:Name>
    <sld:FeatureTypeName>Feature</sld:FeatureTypeName>
    <sld:Rule>
      <sld:RasterSymbolizer>
        <sld:Geometry>
          <ogc:PropertyName>geom</ogc:PropertyName>
        </sld:Geometry>
        <sld:ChannelSelection>
          <sld:GrayChannel>
            <sld:SourceChannelName>1</sld:SourceChannelName>
          </sld:GrayChannel>
        </sld:ChannelSelection>
        <sld:ColorMap>
          <sld:ColorMapEntry color="#ffffff" opacity="0" quantity="-9999"/>
          <sld:ColorMapEntry color="#002DD0" quantity="0.9499817" label="0.9499817 degrees C"/>
          <sld:ColorMapEntry color="#005BA2" quantity="2.5700073"/>
          <sld:ColorMapEntry color="#008C73" quantity="3.1399841"/>
          <sld:ColorMapEntry color="#00B944" quantity="3.5100098"/>
          <sld:ColorMapEntry color="#00E716" quantity="3.7200012"/>
          <sld:ColorMapEntry color="#A0FF00" quantity="3.8900146"/>
          <sld:ColorMapEntry color="#FFFF00" quantity="4.0400085"/>
          <sld:ColorMapEntry color="#FFC814" quantity="4.149994"/>
          <sld:ColorMapEntry color="#FFA000" quantity="4.299988"/>
          <sld:ColorMapEntry color="#FF5B00" quantity="4.470001"/>
          <sld:ColorMapEntry color="#FF0000" quantity="5.1400146" label="5.1400146 degrees C"/>
        </sld:ColorMap>
      </sld:RasterSymbolizer>
    </sld:Rule>
  </sld:FeatureTypeStyle>
</sld:UserStyle>